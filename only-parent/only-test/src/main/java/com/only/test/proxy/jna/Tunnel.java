package com.only.test.proxy.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Tunnel {


    private static ITunnel tunnel = null;
    private final static Object lock = new Object();


    static class TunnelBean implements InvocationHandler{
        private Method[] implmethods = ITunnel.class.getMethods();
        private Map<Method, Method> methodMap = new HashMap<Method, Method>();
        private Method[] methods = TunnelLibrary.INSTANTCE.getClass().getMethods();

        TunnelBean() {
            initReal();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if (name.equals("toString")) {
                return method.invoke(proxy, args);
            } else {
                Method targetMethod = methodMap.get(method);
                if (targetMethod == null) {
                    throw new RuntimeException("Nosuch Method " + targetMethod.getName());
                }
                if (args == null) {
                    args = new Object[]{};
                }
                return targetMethod.invoke(TunnelLibrary.INSTANTCE, args);
            }
        }


        private void initReal() {
            for (Method implmethod : implmethods) {
                String name = implmethod.getName();
                for (Method method : methods) {
                    String tMethod = method.getName();
                    if (name.equals(tMethod)) {
                        methodMap.put(implmethod, method);
                        break;
                    }
                }
            }
        }
    }

    public static ITunnel getBean() {
        if (tunnel == null) {
            synchronized (lock) {
                if (tunnel == null) {
                    tunnel = (ITunnel) Proxy.newProxyInstance(Tunnel.class.getClassLoader(), new Class[]{ITunnel.class}, new TunnelBean());
                }
            }
        }
        return tunnel;
    }



    private interface TunnelLibrary{

        TunnelLibrary INSTANTCE = new TunnelLibrary() {
            @Override
            public void send() {
                System.out.println("内部send");
            }

            @Override
            public void res() {
                System.out.println("内部res");
            }
        };

        void send();

        void res();
    }

}
