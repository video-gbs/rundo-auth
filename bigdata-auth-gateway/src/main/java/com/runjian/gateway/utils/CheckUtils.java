package com.runjian.gateway.utils;

/**
 * @author Miracle
 * @date 2023/6/13 15:12
 */
public class CheckUtils {

    public static boolean checkPath(String path, String validPath){
        String[] validPaths = validPath.split("/");
        String[] paths = path.split("/");

        for (int i = 1; i < validPaths.length; i++){
            if (validPaths[i].equals("**")){
                return true;
            } else if (!validPaths[i].equals(paths[i])) {
                if (validPaths[i].equals("*")){
                    continue;
                }
                return false;
            }
        }
        return true;
    }
}
