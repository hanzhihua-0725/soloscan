package org.soloquest.soloscan.utils;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.SoloscanOptions;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class MiscUtils {
    static public RuntimeException sneakyThrow(final Throwable t) {
        if (t == null) {
            throw new NullPointerException();
        }
        MiscUtils.<RuntimeException>sneakyThrow0(t);
        return null;
    }

    @SuppressWarnings("unchecked")
    static private <T extends Throwable> void sneakyThrow0(final Throwable t) throws T {
        throw (T) t;
    }


    public static void generateClassFile(String className, byte[] bytes) {
        String tempDirPath = SoloscanOptions.getOption(SoloscanOptions.GENERATE_CLASS_ROOT_PATH);
        File tempDir = new File(tempDirPath);
        File classRoot = new File(tempDir, "soloscan_classes");
        classRoot.mkdir();
        String fileName = className + ".class";

        try {
            ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
            OutputStream outStream = new BufferedOutputStream(new FileOutputStream(new File(classRoot, fileName)));


            int byteCount = 0;
            byte[] buffer = new byte[4096];

            for (int bytesRead = 1; (bytesRead = inStream.read(buffer)) != -1; byteCount += bytesRead) {
                outStream.write(buffer, 0, bytesRead);
            }

            outStream.flush();

        } catch (IOException e) {
            log.error("generateClassFile failure:", e);
        }
    }

    public static <T> T forciblyCast(Object argument) {
        return (T) argument;
    }

    public static boolean isEmpty(final String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isBlank(final String str) {
        int length;

        if (str == null || (length = str.length()) == 0) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMethodOverridden(Class<?> clazz, Class<?> interfaceClass, String methodName, Class... classes) {
        try {
            Method interfaceMethod = interfaceClass.getMethod(methodName, classes);
            Method classMethod = clazz.getMethod(methodName, classes);
            return !classMethod.equals(interfaceMethod) && !Modifier.isAbstract(classMethod.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public final static Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");

    public static void applyPlaceHolder(Map<String, String> expressionMap, Map<String, String> placeHolderMap) {
        if (placeHolderMap == null || placeHolderMap.size() == 0) {
            return;
        }
        expressionMap.forEach((k, v) -> {
            if (v != null && v.contains("{{") && v.contains("}}")) {
                StringBuffer sb = new StringBuffer();
                java.util.regex.Matcher matcher = pattern.matcher(v);
                while (matcher.find()) {
                    String placeHolder = matcher.group(1);
                    String value = placeHolderMap.get(placeHolder);
                    if (value != null) {
                        matcher.appendReplacement(sb, value);
                    } else {
                        log.warn("placeHolderMap not contains key:{}", placeHolder);
                    }
                }
                matcher.appendTail(sb);
                expressionMap.put(k, sb.toString());
            }
        });
    }

    public static Map<String, Object> toLowerKey(Map<String, Object> map) {
        if (map == null) {
            return new HashMap<>();
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Set<String> sets = map.keySet();
        for (String key : sets) {
            resultMap.put(key.toLowerCase(), map.get(key));
        }
        return resultMap;
    }

    public static Set<String> findMissingElementsCaseSensitive(List<String> list1, List<String> list2) {
        return list2.stream()
                .filter(element -> !list1.contains(element))
                .collect(Collectors.toSet());
    }

    public static Set<String> findMissingElementsCaseInsensitive(List<String> list1, List<String> list2) {
        List<String> lowerCaseList1 = list1.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return list2.stream()
                .filter(element -> !lowerCaseList1.contains(element.toLowerCase()))
                .collect(Collectors.toSet());
    }

}
