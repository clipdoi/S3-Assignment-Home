package com.s3.friendsmanagement.utils;


import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

    private EmailUtils(){}

    public static Set<String> getEmailsFromText(String text) {
        Set<String> listEmail = new HashSet<>();
        Matcher matcher = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text);
//      ^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$
        while (matcher.find()) {
            listEmail.add(matcher.group());
        }
        return listEmail;
    }

    public static boolean isEmail(String email){
        Matcher matcher = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(email);
        return matcher.find();
    }

}
