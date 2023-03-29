package com.safecode.cyberzone.authorize.service.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.controller.SysUserController;
import com.safecode.cyberzone.authorize.dto.UserDto;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.exception.ParamException;
import com.safecode.cyberzone.authorize.mapper.SysRoleMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.authorize.mapper.SysUserMapper;
import com.safecode.cyberzone.authorize.param.ImportParam;
import com.safecode.cyberzone.authorize.param.MailParam;
import com.safecode.cyberzone.authorize.param.TelParam;
import com.safecode.cyberzone.authorize.param.UserAdminParam;
import com.safecode.cyberzone.authorize.param.UserAdminPsParam;
import com.safecode.cyberzone.authorize.param.UserCenterParam;
import com.safecode.cyberzone.authorize.param.UserNoLoPsParam;
import com.safecode.cyberzone.authorize.param.UserParam;
import com.safecode.cyberzone.authorize.param.UserPsParam;
import com.safecode.cyberzone.authorize.service.SysUserService;
import com.safecode.cyberzone.authorize.utils.BeanValidator;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.authorize.utils.RsaUtil;
import com.safecode.cyberzone.authorize.utils.StringUtil;
import com.safecode.cyberzone.base.log.SysLog;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public synchronized SysUser save(UserParam param) {

        try {
            String password = RsaUtil.decrypt(param.getPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            String confirmPassword = RsaUtil.decrypt(param.getConfirmPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            param.setPassword(password);
            param.setConfirmPassword(confirmPassword);
        } catch (Exception e) {
            throw new ParamException("RSA algorithm not supported");
        }

        BeanValidator.check(param);
        if (!checkPasswordEqual(param.getPassword(), param.getConfirmPassword())) {
            throw new ParamException("密码输入不一致");
        }
        if (checkAccountExist(param.getAccount(), param.getId())) {
            throw new ParamException("账号已被占用");
        }
        if (StringUtils.isNotBlank(param.getMail())) {
            MailParam mailParam = MailParam.builder().mail(param.getMail()).build();
            BeanValidator.check(mailParam);
            if (checkEmailExist(param.getMail(), param.getId())) {
                throw new ParamException("邮箱已被占用");
            }
        }
        if (StringUtils.isNotBlank(param.getTelephone())) {
            TelParam telParam = TelParam.builder().telephone(param.getTelephone()).build();
            BeanValidator.check(telParam);
            if (checkTelephoneExist(param.getTelephone(), param.getId())) {
                throw new ParamException("电话已被占用");
            }
        }
        String password = passwordEncoder.encode(param.getPassword());
        SysUser sysUser = SysUser.builder().account(param.getAccount()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).password(password).deptId(param.getDeptId())
                .deptName(param.getDeptName()).gender(param.getGender()).company(param.getCompany()).job(param.getJob())
                .icon(param.getIcon()).status(param.getStatus())
                .credentialsExpired(new Date(
                        LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .oldPassword(password).signature(param.getSignature()).faceId(param.getFaceId())
                .facePerm(param.getFacePerm()).remark(param.getRemark()).build();
        sysUser.setOperateIp(IpUtil.getRemoteIp(request));
        sysUser.setCreater(AuthHolder.getCurrentUsername());
        sysUser.setCreateTime(new Date());

        sysUserMapper.insertSelective(sysUser);
        updateRoleByUserId(param.getRoleIds(), sysUser.getId());
        return sysUser;
    }

    @Override
    public void update(UserAdminParam param) {
        BeanValidator.check(param);
        if (checkAccountExist(param.getAccount(), param.getId())) {
            throw new ParamException("账号已被占用");
        }
        if (StringUtils.isNotBlank(param.getMail())) {
            MailParam mailParam = MailParam.builder().mail(param.getMail()).build();
            BeanValidator.check(mailParam);
            if (checkEmailExist(param.getMail(), param.getId())) {
                throw new ParamException("邮箱已被占用");
            }
        }
        if (StringUtils.isNotBlank(param.getTelephone())) {
            TelParam telParam = TelParam.builder().telephone(param.getTelephone()).build();
            BeanValidator.check(telParam);
            if (checkTelephoneExist(param.getTelephone(), param.getId())) {
                throw new ParamException("电话已被占用");
            }
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId())
                .deptName(param.getDeptName()).gender(param.getGender()).company(param.getCompany()).job(param.getJob())
                .icon(param.getIcon()).status(param.getStatus()).signature(param.getSignature())
                .facePerm(param.getFacePerm()).remark(param.getRemark()).build();
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        updateRoleByUserId(param.getRoleIds(), param.getId());
    }

    @Override
    public PageInfo<UserDto> list(Pageable pageable, UserDto dto) {

        Integer userId = AuthHolder.getCurrentUserId();
        Integer fixRoleId = dto.getFixRoleId();
        // 根据固定角色ID查用户ID 成员管理用
        List<Integer> userIdListByRoleIdList = sysRoleUserMapper
                .getUserIdListByRoleIdList(Lists.<Integer>newArrayList(fixRoleId));

        // 超级管理员
        if (sysCoreService.isSuperAdmin(userId)) {
            PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
            Page<UserDto> users = sysUserMapper.new2List(dto.getAccount(), dto.getUsername(), dto.getPlatformName(),
                    dto.getRoleId());
            for (UserDto userDto : users) {
                if (userIdListByRoleIdList.contains(userDto.getId())) {
                    userDto.setChecked(true);
                }
                // 根据用户ID查角色
                List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(userDto.getId());
                if (CollectionUtils.isNotEmpty(roleIds)) {
                    List<SysRole> roleList = sysRoleMapper.getByIdList(roleIds);
                    List<String> roleNames = roleList.stream().map(sysRole -> sysRole.getName())
                            .collect(Collectors.toList());
                    List<String> platformNames = roleList.stream().map(sysRole -> sysRole.getPlatformName())
                            .collect(Collectors.toList());
                    disRoleAndPlatform(roleNames, platformNames, userDto);
                }
            }
            return new PageInfo<UserDto>(users);
        }

        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new PageInfo<UserDto>(Lists.newArrayList());
        }
        // 根据角色ID查角色
        List<SysRole> roleLists = sysRoleMapper.getByIdList(roleIdList);

        List<String> adminList = roleLists.stream().map(role -> role.getAdmin()).collect(Collectors.toList());

        String admin = String.join(",", adminList).replace("管理员", "");
        List<String> newAdminList = Arrays.asList(admin.split(","));

        // 运营管理中心管理员
        if (newAdminList.contains("靶场用户")) {
            PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
            Page<UserDto> users = sysUserMapper.new2List(dto.getAccount(), dto.getUsername(), dto.getPlatformName(),
                    dto.getRoleId());
            for (UserDto userDto : users) {
                if (userIdListByRoleIdList.contains(userDto.getId())) {
                    userDto.setChecked(true);
                }
                // 根据用户ID查角色
                List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(userDto.getId());
                if (CollectionUtils.isNotEmpty(roleIds)) {
                    List<SysRole> roleList = sysRoleMapper.getByIdList(roleIds);
                    List<String> roleNames = roleList.stream().map(sysRole -> sysRole.getName())
                            .collect(Collectors.toList());
                    List<String> platformNames = roleList.stream().map(sysRole -> sysRole.getPlatformName())
                            .collect(Collectors.toList());
                    disRoleAndPlatform(roleNames, platformNames, userDto);
                }
            }
            return new PageInfo<UserDto>(users);
        }

        // 各平台管理员 自定义管理员
        List<Integer> userIdList = onlyAdmin(roleIdList, newAdminList);
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Page<UserDto> users = sysUserMapper.newList(userIdList, dto.getAccount(), dto.getUsername(),
                dto.getPlatformName(), dto.getRoleId());

        for (UserDto userDto : users) {
            if (userIdListByRoleIdList.contains(userDto.getId())) {
                userDto.setChecked(true);
            }
            // 根据用户ID查角色
            List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(userDto.getId());
            if (CollectionUtils.isNotEmpty(roleIds)) {
                List<SysRole> roleList = sysRoleMapper.getByIdList(roleIds);
                List<String> roleNames = roleList.stream().map(sysRole -> sysRole.getName())
                        .collect(Collectors.toList());
                List<String> platformNames = roleList.stream().map(sysRole -> sysRole.getPlatformName())
                        .collect(Collectors.toList());
                disRoleAndPlatform(roleNames, platformNames, userDto);
            }
        }
        return new PageInfo<UserDto>(users);
    }

    // 各平台管理员 自定义管理员
    public List<Integer> onlyAdmin(List<Integer> roleIdList, List<String> newAdminList) {
        // 根据admin查角色
        List<Integer> userIdList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(StringUtil.splitToListString(StringUtils.join(newAdminList, ",")))) {
            List<SysRole> adminRoleList = sysRoleMapper.getByAdminList(newAdminList);
            Set<Integer> set = adminRoleList.stream().map(role -> role.getId()).collect(Collectors.toSet());
            userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(new ArrayList<>(set));
        }

        // 没有指定角色的用户列表
        List<Integer> noRoleUserIdList = Lists.newArrayList();
        List<SysUser> allUserList = sysUserMapper.getAll();
        List<Integer> allUserIds = allUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toList());

        // 没有角色的用户列表
        for (Integer uId : allUserIds) {
            List<Integer> roleId = sysRoleUserMapper.getRoleIdListByUserId(uId);
            if (CollectionUtils.isEmpty(roleId)) {
                noRoleUserIdList.add(uId);
            }
        }

        // 没有平台的角色---对应的用户
        List<SysRole> noPlatformRoleList = sysRoleMapper.getNoPlatform();
        List<Integer> noPlatformRoleIdList = noPlatformRoleList.stream().map(sysRole -> sysRole.getId())
                .collect(Collectors.toList());
        List<Integer> noPlatformRoleUserIdList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(noPlatformRoleIdList)) {
            noPlatformRoleUserIdList = sysRoleUserMapper.getUserIdListByRoleIdList(noPlatformRoleIdList);
        }

        userIdList.addAll(noPlatformRoleUserIdList);
        userIdList.addAll(noRoleUserIdList);
        return userIdList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

    @Override
    public void delete(int userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        Preconditions.checkNotNull(sysUser, "待删除的用户不存在");
        // 删除人脸模型
        String faceId = sysUser.getFaceId();
        if (StringUtils.isNotBlank(faceId)) {
            deleteFace(File.separator + "home" + File.separator + "xuq" + File.separator + "face_id" + File.separator
                    + "facelib" + File.separator + faceId + File.separator);
        }
        sysUserMapper.deleteByPrimaryKey(userId);
        sysRoleUserMapper.deleteByUserId(userId);
    }

    @Override
    public SysUser selectByPrimaryKey(int userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        return sysUser;
    }

    @Override
    public UserDto id(Integer id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        UserDto userDto = UserDto.adapt(sysUser);
        List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(id);
        userDto.setRoleIdList(roleIds);
        return userDto;
    }

    /**
     * 用户详情
     */
    @Override
    public UserDto info(Integer id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysUser, "该用户不存在");
        UserDto userDto = UserDto.adapt(sysUser);
        // 根据用户ID查角色
        List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(sysUser.getId());
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<SysRole> roleList = sysRoleMapper.getByIdList(roleIds);
            List<String> roleNames = roleList.stream().map(sysRole -> sysRole.getName()).collect(Collectors.toList());
            List<String> platformNames = roleList.stream().map(sysRole -> sysRole.getPlatformName())
                    .collect(Collectors.toList());
            disRoleAndPlatform(roleNames, platformNames, userDto);
        }
        return userDto;
    }

    @Override
    public void batchDelete(List<Integer> userIdList) {

        List<SysUser> userList = sysUserMapper.getByIdList(userIdList);
        List<String> faceIdList = userList.stream().map(sysUser -> sysUser.getFaceId()).collect(Collectors.toList());
        for (String faceId : faceIdList) {
            if (StringUtils.isNotBlank(faceId)) {
                deleteFace(File.separator + "home" + File.separator + "xuq" + File.separator + "face_id"
                        + File.separator + "facelib" + File.separator + faceId + File.separator);
            }
        }
        sysUserMapper.batchDelete(userIdList);
        // 删除用户对应的角色
        for (Integer userId : userIdList) {
            sysRoleUserMapper.deleteByUserId(userId);
        }
    }

    @Override
    public void export(OutputStream out, Integer deptId, Pageable pageable) throws Exception {
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("用户");

        // 设置列宽
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);

        // 创建行
        HSSFRow titleRow = sheet.createRow(0);
        // 设置标题名称
        titleRow.createCell(0).setCellValue("ID");
        titleRow.createCell(1).setCellValue("账号");
        titleRow.createCell(2).setCellValue("姓名");
        titleRow.createCell(3).setCellValue("手机号");
        titleRow.createCell(4).setCellValue("邮箱");
        titleRow.createCell(5).setCellValue("单位");
        titleRow.createCell(6).setCellValue("职务");
        titleRow.createCell(7).setCellValue("个性签名");
        titleRow.createCell(8).setCellValue("备注");

        // 获取用户数据
        int rowIndex = 1;
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Page<SysUser> page = sysUserMapper.getPageByDeptId(deptId);
        for (SysUser sysUser : page) {
            HSSFRow row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(sysUser.getId());
            row.createCell(1).setCellValue(sysUser.getAccount());
            row.createCell(2).setCellValue(sysUser.getUsername());
            row.createCell(3).setCellValue(sysUser.getTelephone());
            row.createCell(4).setCellValue(sysUser.getMail());
            row.createCell(5).setCellValue(sysUser.getCompany());
            row.createCell(6).setCellValue(sysUser.getJob());
            row.createCell(7).setCellValue(sysUser.getSignature());
            row.createCell(8).setCellValue(sysUser.getRemark());
            rowIndex++;
        }
        book.write(out);
        book.close();
    }

    @SuppressWarnings("resource")
    @Override
    public Object doImport(InputStream input) throws Exception {

        List<ImportParam> listParam = Lists.newArrayList();
        // 从流中得到工作簿
        HSSFWorkbook book = new HSSFWorkbook(input);
        // 得到第一个工作表
        HSSFSheet sheet = book.getSheetAt(0);
        // 获取最后一行行号
        int lastRowNum = sheet.getLastRowNum();

        if (500 <= lastRowNum) {
            throw new ParamException("上传用户不能超过500个");
        }

        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            HSSFRow row = sheet.getRow(rowIndex);
            if (row != null) {
                ImportParam param = new ImportParam();
                if (row.getCell(0) != null) {
                    row.getCell(0).setCellType(CellType.STRING);
                    param.setAccount(row.getCell(0).getStringCellValue());
                }
                if (row.getCell(1) != null) {
                    row.getCell(1).setCellType(CellType.STRING);
                    param.setUsername(row.getCell(1).getStringCellValue());
                }
                if (row.getCell(2) != null) {
                    row.getCell(2).setCellType(CellType.STRING);
                    String gender = row.getCell(2).getStringCellValue();
                    param.setGender(gender);
                }
                if (row.getCell(3) != null) {
                    row.getCell(3).setCellType(CellType.STRING);
                    param.setDeptName(row.getCell(3).getStringCellValue());
                }
                if (row.getCell(4) != null) {
                    row.getCell(4).setCellType(CellType.STRING);
                    param.setJob(row.getCell(4).getStringCellValue());
                }
                if (row.getCell(5) != null) {
                    row.getCell(5).setCellType(CellType.STRING);
                    param.setMail(row.getCell(5).getStringCellValue());
                }
                if (row.getCell(6) != null) {
                    row.getCell(6).setCellType(CellType.STRING);
                    param.setTelephone(row.getCell(6).getStringCellValue());
                }
                if (row.getCell(7) != null) {
                    row.getCell(7).setCellType(CellType.STRING);
                    String facePerm = row.getCell(7).getStringCellValue();
                    param.setFacePerm(facePerm);
                }
                if (StringUtils.isBlank(param.getAccount()) && StringUtils.isBlank(param.getUsername())
                        && StringUtils.isBlank(param.getGender()) && StringUtils.isBlank(param.getDeptName())
                        && StringUtils.isBlank(param.getJob()) && StringUtils.isBlank(param.getMail())
                        && "".equals(param.getMail()) && "".equals(param.getTelephone())
                        && StringUtils.isBlank(param.getTelephone()) && StringUtils.isBlank(param.getFacePerm())) {
                } else {
                    listParam.add(param);
                }
            }
        }
        return imports(listParam);
    }

    @Override
    public Object imports(List<ImportParam> listParam) {

        Set<String> setAccount = Sets.newHashSet();
        Set<String> setMail = Sets.newHashSet();
        Set<String> setTel = Sets.newHashSet();
        List<ImportParam> errorList = Lists.newArrayList();
        List<ImportParam> trueList = Lists.newArrayList();
        List<ImportParam> checkList = Lists.newArrayList();
        for (ImportParam param : listParam) {
            StringBuffer buffer = new StringBuffer();
            // 1,非空判断，account，username
            checkIsNotAccountAndUsername(param.getAccount(), param.getUsername(), buffer);
            // 2,性别，人脸校验
            checkGenderAndFacePerm(param.getGender(), param.getFacePerm(), buffer);
            // 3,校验表格
            checkExcel(param.getAccount(), param.getMail(), param.getTelephone(), buffer, setAccount, setMail, setTel);
            // 3.1 表格校验部门职务
            checkExcelDepAndJob(param.getDeptName(), param.getJob(), buffer);
            // 4,数据库校验
            checkMySQL(param.getAccount(), param.getId(), param.getMail(), param.getTelephone(), buffer);
            // 有异常
            if (StringUtils.isNotBlank(buffer.toString())) {
                errorList.add(param);
                param.setResult(buffer.toString());
            }
            // 无异常
            if (StringUtils.isBlank(buffer.toString())) {
                trueList.add(param);
                param.setResult("√");
            }
        }
        checkList.addAll(errorList);
        checkList.addAll(trueList);
        return checkList;
    }

    @Override
    public ImportParam[] batchImport(ImportParam[] params) {
        for (ImportParam param : params) {
            BeanValidator.check(param);
            if (checkAccountExist(param.getAccount(), param.getId())) {
                throw new ParamException("账号已被占用");
            }
            if (StringUtils.isNotBlank(param.getGender())) {
                if (!param.getGender().equals("男") && !param.getGender().equals("女")) {
                    throw new ParamException("性别输入不合法");
                }
            }
            if (StringUtils.isNotBlank(param.getFacePerm())) {
                if (!param.getFacePerm().equals("是") && !param.getFacePerm().equals("否")) {
                    throw new ParamException("人脸认证输入不合法");
                }
            }
            if (StringUtils.isNotBlank(param.getMail())) {
                MailParam paramMail = MailParam.builder().mail(param.getMail()).build();
                BeanValidator.check(paramMail);
                if (checkEmailExist(param.getMail(), param.getId())) {
                    throw new ParamException("邮箱已被占用");
                }
            }
            if (StringUtils.isNotBlank(param.getTelephone())) {
                TelParam telParam = TelParam.builder().telephone(param.getTelephone()).build();
                BeanValidator.check(telParam);
                if (checkTelephoneExist(param.getTelephone(), param.getId())) {
                    throw new ParamException("电话已被占用");
                }
            }
        }
        // 都通过
        for (ImportParam param : params) {
            String password = passwordEncoder.encode("safecode@123");
            SysUser sysUser = SysUser.builder().account(param.getAccount().trim()).username(param.getUsername().trim())
                    .telephone(param.getTelephone()).mail(param.getMail()).password(password)
                    .deptName(param.getDeptName()).gender("女".equals(param.getGender()) ? 0 : 1).job(param.getJob())
                    .credentialsExpired(new Date(LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault())
                            .toInstant().toEpochMilli()))
                    .oldPassword(password).facePerm("是".equals(param.getFacePerm()) ? 1 : 0).build();
            sysUser.setOperateIp(IpUtil.getRemoteIp(request));
            sysUser.setCreater(AuthHolder.getCurrentUsername());
            sysUser.setCreateTime(new Date());
            sysUserMapper.insertSelective(sysUser);
        }
        return params;
    }

    @Override
    public void resetPwd(UserAdminPsParam param) {

        try {
            String mePassword = RsaUtil.decrypt(param.getMePassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            String password = RsaUtil.decrypt(param.getPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            String confirmPassword = RsaUtil.decrypt(param.getConfirmPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            param.setMePassword(mePassword);
            param.setPassword(password);
            param.setConfirmPassword(confirmPassword);
        } catch (Exception e) {
            throw new ParamException("RSA algorithm not supported");
        }

        BeanValidator.check(param);
        // 判断是否是管理员的密码
        Integer currentId = AuthHolder.getCurrentUserId();
        String mePassword = sysUserMapper.selectByPrimaryKey(currentId).getPassword();
        boolean flag = passwordEncoder.matches(param.getMePassword(), mePassword);
        if (!flag) {
            throw new ParamException("您的账号密码错误");
        }

        if (!checkPasswordEqual(param.getPassword(), param.getConfirmPassword())) {
            throw new ParamException("密码输入不一致");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待重置密码的用户不存在");

        // 判断是否与旧密码匹配
        if (checkOldPwdExist(param.getPassword(), before)) {
            throw new ParamException("新密码与旧密码相同,操作失败");
        }
        String newPwd = passwordEncoder.encode(param.getPassword());

        String oldPwd = countPwd(before.getOldPassword(), newPwd);

        SysUser after = SysUser.builder().id(param.getId()).credentialsNonExpired(1)
                .credentialsExpired(new Date(
                        LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .password(newPwd).oldPassword(oldPwd).build();
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public void updatePwd(UserPsParam param, int currentUserId) {

        try {
            String oldPwd = RsaUtil.decrypt(param.getOldPwd(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            String password = RsaUtil.decrypt(param.getPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            String confirmPassword = RsaUtil.decrypt(param.getConfirmPassword(),
                    RsaUtil.getPrivateKey(SysUserController.class.getResourceAsStream("/rsa/rsa_1024_priv.pem")));
            param.setOldPwd(oldPwd);
            param.setPassword(password);
            param.setConfirmPassword(confirmPassword);
        } catch (Exception e) {
            throw new ParamException("RSA algorithm not supported");
        }

        BeanValidator.check(param);
        SysUser before = sysUserMapper.selectByPrimaryKey(currentUserId);
        Preconditions.checkNotNull(before, "待重置密码的用户不存在");

        if (!passwordEncoder.matches(param.getOldPwd(), before.getPassword())) {
            throw new ParamException("原密码输入不一致");
        }
        if (!checkPasswordEqual(param.getPassword(), param.getConfirmPassword())) {
            throw new ParamException("密码输入不一致");
        }
        // 判断旧密码是否存在
        if (checkOldPwdExist(param.getPassword(), before)) {
            throw new ParamException("新密码与旧密码相同,操作失败");
        }
        String newPwd = passwordEncoder.encode(param.getPassword());
        String oldPwd = countPwd(before.getOldPassword(), newPwd);
        SysUser after = SysUser.builder().id(currentUserId).credentialsNonExpired(1)
                .credentialsExpired(new Date(
                        LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .password(newPwd).oldPassword(oldPwd).build();
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);

    }

    @Override
    public void updateCredentials(UserNoLoPsParam param, HttpServletRequest request) {
        BeanValidator.check(param);
        SysUser before = sysUserMapper.findByKeyword(param.getKeyword());
        Preconditions.checkNotNull(before, "待重置密码的用户不存在");

        if (!passwordEncoder.matches(param.getOldPwd(), before.getPassword())) {
            throw new ParamException("原密码输入不一致");
        }
        if (!checkPasswordEqual(param.getPassword(), param.getConfirmPassword())) {
            throw new ParamException("密码输入不一致");
        }
        // 判断是否与旧密码匹配
        if (checkOldPwdExist(param.getPassword(), before)) {
            throw new ParamException("新密码与旧密码相同,操作失败");
        }

        String newPwd = passwordEncoder.encode(param.getPassword());
        String oldPwd = countPwd(before.getOldPassword(), newPwd);
        SysUser after = SysUser.builder().id(before.getId()).credentialsNonExpired(1)
                .credentialsExpired(new Date(
                        LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .password(newPwd).oldPassword(oldPwd).build();
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperator(param.getKeyword());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public Map<String, Object> center() {

        Map<String, Object> map = Maps.newHashMap();

        Integer currentUserId = AuthHolder.getCurrentUserId();
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(currentUserId);
        UserCenterParam user = new UserCenterParam();
        BeanUtils.copyProperties(sysUser, user);
        map.put("user", user);

        // 根据用户ID查角色
        List<String> displatformName = Lists.newArrayList();
        List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(currentUserId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<SysRole> roleList = sysRoleMapper.getByIdList(roleIds);
            List<String> platformNames = roleList.stream().map(sysRole -> sysRole.getPlatformName())
                    .collect(Collectors.toList());
            displatformName = Arrays.asList(String.join(",", platformNames).split(",")).stream().distinct()
                    .collect(Collectors.toList());
        }
        map.put("permission", displatformName);

        SysLog sysLog = sysUserMapper.getLoginInfo(AuthHolder.getCurrentUsername());
        map.put("login", sysLog);
        return map;
    }

    @Override
    public void updateCenter(UserCenterParam param) {
        param.setId(AuthHolder.getCurrentUserId());
        BeanValidator.check(param);
        if (checkAccountExist(param.getAccount(), param.getId())) {
            throw new ParamException("账号已被占用");
        }
        if (StringUtils.isNotBlank(param.getMail())) {
            MailParam mailParam = MailParam.builder().mail(param.getMail()).build();
            BeanValidator.check(mailParam);
            if (checkEmailExist(param.getMail(), param.getId())) {
                throw new ParamException("邮箱已被占用");
            }
        }
        if (StringUtils.isNotBlank(param.getTelephone())) {
            TelParam telParam = TelParam.builder().telephone(param.getTelephone()).build();
            BeanValidator.check(telParam);
            if (checkTelephoneExist(param.getTelephone(), param.getId())) {
                throw new ParamException("电话已被占用");
            }
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).deptName(param.getDeptName())
                .gender(param.getGender()).job(param.getJob()).icon(param.getIcon()).build();
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
    }

    public boolean checkTelephoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkAccountExist(String account, Integer userId) {
        return sysUserMapper.countByAccount(account, userId) > 0;
    }

    public boolean checkPasswordEqual(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void checkIsNotAccountAndUsername(String account, String username, StringBuffer buffer) {
        String patternA = "[\u4e00-\u9fa5_a-zA-Z0-9_]{2,50}";
        if (StringUtils.isNotBlank(account)) {
            if (!Pattern.matches(patternA, account)) {
                buffer.append("账号须由数字、英文字母、汉字组成,长度为2-50位;");
            }
        } else {
            buffer.append("账号须由数字、英文字母、汉字组成,长度为2-50位;");
        }
        String patternB = "[\u4e00-\u9fa5_a-zA-Z0-9_]{1,10}";
        if (StringUtils.isNotBlank(username)) {
            if (!Pattern.matches(patternB, username)) {
                buffer.append("姓名须由数字、英文字母、汉字组成,长度为1-10位;");
            }
        } else {
            buffer.append("姓名须由数字、英文字母、汉字组成,长度为1-10位;");
        }
    }

    public void checkGenderAndFacePerm(String gender, String facePerm, StringBuffer buffer) {
        if (StringUtils.isNotBlank(gender)) {
            if (!"男".equals(gender) && !"女".equals(gender)) {
                buffer.append("性别输入不合法;");
            }
        }
        if (StringUtils.isNotBlank(facePerm)) {
            if (!"是".equals(facePerm) && !"否".equals(facePerm)) {
                buffer.append("人脸认证输入不合法;");
            }
        }
    }

    // 去重
    public void disRoleAndPlatform(List<String> roleNames, List<String> platformNames, UserDto userDto) {
        String roleNamesStr = String.join(",", roleNames);
        String platformNameStr = String.join(",", platformNames);

        List<String> disRoleNames = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(roleNamesStr).stream()
                .distinct().collect(Collectors.toList());

        List<String> displatformNames = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(platformNameStr)
                .stream().distinct().collect(Collectors.toList());

        userDto.setRoleName(String.join(",", disRoleNames));
        userDto.setPlatformName(String.join(",", displatformNames));
    }

    public void checkExcel(String account, String mail, String tel, StringBuffer buffer, Set<String> setAccount,
                           Set<String> setMail, Set<String> setTel) {
        int beforeAccount = setAccount.size();
        int beforeMail = setMail.size();
        int beforeTel = setTel.size();
        setAccount.add(account);
        setMail.add(mail);
        setTel.add(tel);
        if (setAccount.size() == beforeAccount) {
            buffer.append("Excel中账号重复;");
        }
        if (StringUtils.isNotBlank(mail)) {
            if (setMail.size() == beforeMail) {
                buffer.append("Excel中邮箱重复;");
            }
        }
        if (StringUtils.isNotBlank(tel)) {
            if (setTel.size() == beforeTel) {
                buffer.append("Excel中电话重复;");
            }
        }
    }

    public void checkExcelDepAndJob(String deptName, String job, StringBuffer buffer) {
        String patternA = "[\u4e00-\u9fa5]{0,15}";
        if (StringUtils.isNotBlank(deptName)) {
            if (!Pattern.matches(patternA, deptName)) {
                buffer.append("部门须由汉字组成,长度为0-15位;");
            }
        }
        String patternB = "[\u4e00-\u9fa5]{0,15}";
        if (StringUtils.isNotBlank(job)) {
            if (!Pattern.matches(patternB, job)) {
                buffer.append("职务须由汉字组成,长度为0-15位;");
            }
        }
    }

    public void checkMySQL(String account, Integer userId, String mail, String tel, StringBuffer buffer) {
        if (checkAccountExist(account, userId)) {
            buffer.append("账号已被占用;");
        }

        if (StringUtils.isNotBlank(mail)) {
            MailParam param = MailParam.builder().mail(mail).build();
            try {
                BeanValidator.check(param);
            } catch (ParamException e) {
                buffer.append("不是一个合法的电子邮件地址;");
            }
            if (checkEmailExist(mail, userId)) {
                buffer.append("邮箱已被占用;");
            }
        }
        if (StringUtils.isNotBlank(tel)) {
            TelParam param = TelParam.builder().telephone(tel).build();
            try {
                BeanValidator.check(param);
            } catch (ParamException e) {
                buffer.append("电话不合法;");
            }
            if (checkTelephoneExist(tel, userId)) {
                buffer.append("电话已被占用;");
            }
        }
    }

    /*
     * 检测新密码是否与旧密码匹配
     */
    public boolean checkOldPwdExist(String newPwd, SysUser before) {

        boolean flag = false;
        String[] oldPwds = before.getOldPassword().split(",");
        for (String oldPwd : oldPwds) {
            if (passwordEncoder.matches(newPwd, oldPwd)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    public String countPwd(String oldPwd, String newPwd) {
        List<String> result = Arrays.asList(StringUtils.split(oldPwd, ","));
        LinkedList<String> list = new LinkedList<>(result);

        if (list.size() < 3) {
            list.add(newPwd);
        } else if (list.size() >= 3) {
            list.removeFirst();
            list.addLast(newPwd);
        }
        return String.join(",", list);
    }

    // 根据用户ID更新角色
    public void updateRoleByUserId(String roleIds, int userId) {
        if (StringUtils.isNotBlank(roleIds)) {
            List<Integer> roleIdList = StringUtil.splitToListInt(roleIds);
            sysRoleUserService.changeUserRoles(userId, roleIdList);
        } else {
            sysRoleUserService.changeUserRoles(userId, Lists.<Integer>newArrayList());
        }
    }

    private void deleteFace(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteFace(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
