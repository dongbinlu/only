package com.only.mvc.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 注意事项
 * 1、提供一个接口
 * 2、在resources目录下创建META-INF/services目录
 * 3、创建一个文件，名字为接口的全路径
 * 4、文件内容为实现类全路径，多个时换行即可，#号为注释符
 * 5、spi实现类必须提供无参构造器
 */
public class TestSPI {

    public static void main(String[] args) {

        // 第一步：把我们的接口类型保存到ServiceLoader  service变量中
        // 创建了一个lazyIterator对象（把我们的接口保存，保存我们的classLoader）
        ServiceLoader<IParseDoc> iParseDocs = ServiceLoader.load(IParseDoc.class);

        // 返回我们懒加载的迭代器对象
        Iterator<IParseDoc> iterator = iParseDocs.iterator();
        while (iterator.hasNext()) {
            iterator.next().parse();
        }

    }

}
