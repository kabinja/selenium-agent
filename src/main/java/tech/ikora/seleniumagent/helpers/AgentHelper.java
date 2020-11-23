package tech.ikora.seleniumagent.helpers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class AgentHelper {
    public static String getCurrentUrl(Object driver){
        String url;

        try {
            Method getCurrentDom = driver.getClass().getMethod("getCurrentUrl");
            url = (String)getCurrentDom.invoke(driver);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            url =  String.format("Failed to load current url: %s", e.getMessage());
        }

        return url;
    }

    public static String getDom(Object driver){
        String dom;

        try {
            Method getCurrentDom = driver.getClass().getMethod("executeAsyncScript", String.class, Object[].class);
            dom = (String)getCurrentDom.invoke(driver, JsCode.getCurrentDomWithStyles, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            dom = String.format("Failed to load dom: %s", e.getMessage());
        }

        return dom;
    }

    public static String getWindowWidth(Object driver){
        int width;

        try {
            width = getSize(driver, "getWidth");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to get width: " + e.getMessage());
            width = -1;
        }

        return String.valueOf(width);
    }

    public static String getWindowHeight(Object driver){
        int height;

        try {
            height = getSize(driver, "getHeight");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Failed to get height: " + e.getMessage());
            height = -1;
        }

        return String.valueOf(height);
    }

    public static String getStackTrace(){
        final StringBuilder stringBuilder = new StringBuilder();

        String prefix = "";
        for(StackTraceElement st: Thread.currentThread().getStackTrace()){
            stringBuilder.append(prefix);
            stringBuilder.append(st.getClassName());
            stringBuilder.append(":");
            stringBuilder.append(st.getMethodName());
            stringBuilder.append(":");
            stringBuilder.append(st.getLineNumber());

            prefix = ";";
        }

        return stringBuilder.toString();
    }

    public static String getFailure(Throwable throwable){
        return throwable != null ? throwable.getClass().getName() : "none";
    }

    public static void initializeFrame(DataOutputStream out) throws IOException {
        out.writeChar('f');
    }

    public static void sendMessage(DataOutputStream out, char type, String payload) throws IOException {
        final byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);

        out.writeChar(type);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    private static int getSize(Object driver, String getValueName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method manage = driver.getClass().getMethod("manage");
        Object options = manage.invoke(driver);
        Method getWindow = options.getClass().getMethod("window");
        Object window = getWindow.invoke(options);
        Method getSize = window.getClass().getMethod("getSize");
        Object dimension = getSize.invoke(window);
        Method getValue = dimension.getClass().getMethod(getValueName);

        return (int)getValue.invoke(dimension);
    }
}
