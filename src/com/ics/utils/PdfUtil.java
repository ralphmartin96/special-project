package com.ics.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Ref Item:SRC201205141200 Date:2012.05.15 Developer:Kelvin Feng
// the static methods put in here, 2011.3.23 collected by Leingy

public class PdfUtil {

    // character, so can't ignore them
    public static int findRowIndex(String key, String allTxt) {
        String[] allRows = allTxt.split("\n");
        int row = -1;
        if (key.contains("||")) {
            for (int i = 0; i < key.split("\\|\\|").length; i++) {
                String keyWord = key.split("\\|\\|")[i];
                String[] keyWords = { keyWord };
                if (keyWord.contains(","))
                    keyWords = keyWord.split(",");
                int flag = 0;// count of matched keys
                int startRowIndex = -1;
                int startNo = keyWords.length;
                for (int m = 0; m < allRows.length; ++m) {
                    flag = 0;
                    for (int j = 0; j < startNo; ++j) {
                        if (!allRows[m].toUpperCase().contains(
                                keyWords[j].toUpperCase()))
                            break;
                        else
                            flag++;
                    }
                    if (flag == startNo) {
                        startRowIndex = m;
                        break;
                    }
                }
                row = startRowIndex;
                if (row > -1)
                    break;
            }
        } else {
            String[] keyWords = { key };
            if (key.contains(","))
                keyWords = key.split(",");
            int flag = 0;// count of matched keys
            int startRowIndex = -1;
            int startNo = keyWords.length;
            for (int m = 0; m < allRows.length; ++m) {
                flag = 0;
                for (int j = 0; j < startNo; ++j) {
                    if (!allRows[m].toUpperCase().contains(
                            keyWords[j].toUpperCase()))
                        break;
                    else
                        flag++;
                }
                if (flag == startNo) {
                    startRowIndex = m;
                    break;
                }
            }
            row = startRowIndex;
        }
        return row;
    }

    // return the key's index in allTxt from startRow
    public static int findRowIndexFrom(String key, int startRow, String allTxt) {
        String[] allRows = allTxt.split("\n");
        int row = -1;
        if (key.contains("||")) {
            for (int i = 0; i < key.split("\\|\\|").length; i++) {
                String keyWord = key.split("\\|\\|")[i];
                String[] keyWords = { keyWord };
                if (keyWord.contains(","))
                    keyWords = keyWord.split(",");
                int flag = 0;// count of matched keys
                int startRowIndex = -1;
                int startNo = keyWords.length;
                for (int m = startRow; m < allRows.length; ++m) {
                    flag = 0;
                    for (int j = 0; j < startNo; ++j) {
                        if (!allRows[m].toUpperCase().contains(
                                keyWords[j].toUpperCase()))
                            break;
                        else
                            flag++;
                    }
                    if (flag == startNo) {
                        startRowIndex = m;
                        break;
                    }
                }
                row = startRowIndex;
                if (row > -1)
                    break;
            }
        } else {
            String[] keyWords = { key };
            if (key.contains(","))
                keyWords = key.split(",");
            int flag = 0;// count of matched keys
            int startRowIndex = -1;
            int startNo = keyWords.length;
            for (int m = startRow; m < allRows.length; ++m) {
                flag = 0;
                for (int j = 0; j < startNo; ++j) {
                    if (!allRows[m].toUpperCase().contains(
                            keyWords[j].toUpperCase()))
                        break;
                    else
                        flag++;
                }
                if (flag == startNo) {
                    startRowIndex = m;
                    break;
                }
            }
            row = startRowIndex;
        }
        return row;
    }

    // count the numbers in string
    public static int countNumber(String str) {
        int count = 0;

        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                count++;
            }
        }

        return count;
    }

    // get area value by row index
    public static String getAreaValue(int startRow, int endRow, int beginPos,
                                      int endPos, String allTxt) {
        String strTmp = null;
        StringBuffer sb = new StringBuffer();
        String allRows[] = allTxt.split("\n");
        int tag_end;
        for (int i = startRow; i <= endRow && i < allRows.length; ++i) {
            if (allRows[i] != null && !"".equals(allRows[i])
                    && allRows[i].length() > beginPos) {
                if (allRows[i].length() <= endPos) {
                    tag_end = allRows[i].length();
                } else {
                    tag_end = endPos + 1;
                }
                strTmp = allRows[i].substring(beginPos, tag_end);
                if (strTmp != null && !"".equals(strTmp.trim())
                        && strTmp.trim() != null) {
                    sb.append(strTmp.trim());
                    sb.append("\n");
                }
            }
        }
        if (sb != null)
            strTmp = sb.toString().trim();
        return strTmp;
    }

    // get area value by key words
    public static String getAreaValue(String startRow, String endRow,
                                      int startRowChange, int endRowChange, int startColIndex,
                                      int endColIndex, String allTxt) {
        int startRowIndex = 0;
        int endRowIndex = 0;
        String strTmp = "";

        startRowIndex = PdfUtil.findRowIndex(startRow, allTxt);
        endRowIndex = PdfUtil.findRowIndex(endRow, allTxt);

        if (startRowIndex >= 0 && endRowIndex >= 0) {
            startRowIndex = startRowIndex + startRowChange;
            endRowIndex = endRowIndex + endRowChange;
            strTmp = PdfUtil.getAreaValue(startRowIndex, endRowIndex,
                    startColIndex, endColIndex, allTxt);
        }
        return strTmp;
    }

    // get area value smartly by row index
    public static String getAreaValueSmart(int startRow, int endRow,
                                           int beginPos, int endPos, String allTxt) {
        String strTmp = null;
        StringBuffer sb = new StringBuffer();
        String allRows[] = allTxt.split("\n");
        int tag_end;
        for (int i = startRow; i <= endRow && i < allRows.length; ++i) {
            if (allRows[i] != null && !"".equals(allRows[i])
                    && allRows[i].length() > beginPos) {
                if (allRows[i].length() <= endPos) {
                    tag_end = allRows[i].length();
                } else {
                    tag_end = getEndPosSmart(allRows[i], endPos);
                }
                strTmp = allRows[i].substring(beginPos, tag_end);
                if (strTmp != null && !"".equals(strTmp.trim())
                        && strTmp.trim() != null) {
                    sb.append(strTmp.trim());
                    sb.append("\n");
                }
            }
        }
        if (sb != null)
            strTmp = sb.toString().trim();
        return strTmp;
    }

    // get area value smartly by key words
    public static String getAreaValueSmart(String startRow, String endRow,
                                           int startRowChange, int endRowChange, int startColIndex,
                                           int endColIndex, String allTxt) {
        int startRowIndex = 0;
        int endRowIndex = 0;
        String strTmp = "";

        startRowIndex = PdfUtil.findRowIndex(startRow, allTxt);
        endRowIndex = PdfUtil.findRowIndex(endRow, allTxt);

        if (startRowIndex >= 0 && endRowIndex >= 0) {
            startRowIndex = startRowIndex + startRowChange;
            endRowIndex = endRowIndex + endRowChange;
            strTmp = PdfUtil.getAreaValueSmart(startRowIndex, endRowIndex,
                    startColIndex, endColIndex, allTxt);
        }
        return strTmp;
    }

    private static int getEndPosSmart(String str, int endPos) {
        str = str.substring(endPos, str.length());
        int endIndex;
        if (str.contains(" ")) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == ' ') {
                    endIndex = endPos + i;
                    return endIndex;
                }
            }
        } else {
            endIndex = endPos + str.length();
            return endIndex;
        }
        return 0;
    }

    // get the information without non-number characters
    public static String cutNumberChar(String message) {
        message = message.replace(",", "");
        message = message.replace(".", "");
        StringBuffer str = new StringBuffer();
        char[] arr = message.toCharArray();
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] >= '0' && arr[i] <= '9') {
                arr[i] = ' ';
            }
            str.append(arr[i]);
        }

        return str.toString().replace(" ", "").trim();
    }

    // get the infromation without non-number characters
    public static String cutNonNumberChar(String message) {
        message = message.replace(",", "");
        StringBuffer str = new StringBuffer();
        char[] arr = message.toCharArray();
        for (int i = 0; i < arr.length; ++i) {
            if ((arr[i] < '0' || arr[i] > '9') && arr[i] != '.') {
                arr[i] = ' ';
            }
            str.append(arr[i]);
        }

        return str.toString().replace(" ", "").trim();
    }

    // cut duplicated blank
    public static String cutContent(String message) {
        while (message.contains("  "))
            message = message.replace("  ", " ");
        return message;
    }

    // get the pure ASCII
    public static String pureAscii(String strTem) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strTem.length(); i++) {
            String str = strTem.substring(i, i + 1);
            boolean ascii = true;
            for (int j = 0; j < str.length(); j++) {
                char ch = str.charAt(j);
                if (ch >= 127 || ch < 0)
                    ascii = false;
            }
            if (ascii)
                sb.append(strTem.substring(i, i + 1));
        }
        strTem = sb.toString();
        return strTem;
    }

    // find the first key word's column in the key row
    public static int findColIndex(String keyWord, int colChange, String allTxt) {
        int indexOf = -1;
        String[] allRows = allTxt.split("\n");
        int row = PdfUtil.findRowIndex(keyWord, allTxt);
        if (row >= 0) {
            indexOf = allRows[row].toUpperCase().indexOf(
                    keyWord.contains(",") ? keyWord.split(",")[0].toUpperCase()
                            : keyWord.toUpperCase());
        }
        indexOf = indexOf + colChange;
        if (indexOf < 0)
            indexOf = 0;
        return indexOf;
    }

    // auto change line(ignore the word's length is longger than maxLength)
    public static String autoChangeLine(String strTmp, int maxLength) {
        strTmp = strTmp.replace("\r", " ");
        strTmp = strTmp.replace("\n", " ");
        while (strTmp.contains("  "))
            strTmp = strTmp.replace("  ", " ");
        StringBuffer sb = new StringBuffer();
        StringBuffer sbtmp = new StringBuffer();
        String[] strList = strTmp.split(" ");
        for (String tmp : strList) {
            if (sbtmp.length() + tmp.length() > maxLength - 1) {
                sbtmp.delete(0, sbtmp.length());
                sb.append("\n");
            }
            sbtmp.append(tmp).append(" ");
            sb.append(tmp).append(" ");
        }
        strTmp = sb.toString().trim();
        return strTmp;
    }

    public static String cutUselessInformations(String allTxt,
                                                String startRowKey, String endRowKey, int startChange, int endChange) {
        int startRowIndex = 0;
        int endRowIndex = 0;
        // to get the new text
        while ((startRowIndex = PdfUtil.findRowIndexFrom(startRowKey,
                endRowIndex, allTxt)) >= 0
                && (endRowIndex = PdfUtil.findRowIndexFrom(endRowKey,
                startRowIndex, allTxt)) >= 0) {
            if (startRowIndex + startChange > endRowIndex + endChange)
                break;
            String[] tmp = allTxt.split("\n");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < startRowIndex + startChange; ++i) {
                sb.append(tmp[i]).append("\n");
            }
            for (int i = startRowIndex + startChange; i < endRowIndex
                    + endChange + 1; ++i) {
                sb.append("").append("\n");
            }
            for (int i = endRowIndex + endChange + 1; i < tmp.length; ++i) {
                sb.append(tmp[i]).append("\n");
            }
            allTxt = sb.toString().trim();
        }
        return allTxt;
    }

    public static String cutUselessInformationsFrom(String allTxt,
                                                    String startRowKey, String endRowKey, int startChange,
                                                    int endChange, int beginRowIndex) {
        int startRowIndex = 0;
        int endRowIndex = beginRowIndex;
        // to get the new text
        while ((startRowIndex = PdfUtil.findRowIndexFrom(startRowKey,
                endRowIndex, allTxt)) >= 0
                && (endRowIndex = PdfUtil.findRowIndexFrom(endRowKey,
                startRowIndex, allTxt)) >= 0) {
            if (startRowIndex + startChange > endRowIndex + endChange)
                break;
            String[] tmp = allTxt.split("\n");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < startRowIndex + startChange; ++i) {
                sb.append(tmp[i]).append("\n");
            }
            for (int i = startRowIndex + startChange; i < endRowIndex
                    + endChange + 1; ++i) {
                sb.append("").append("\n");
            }
            for (int i = endRowIndex + endChange + 1; i < tmp.length; ++i) {
                sb.append(tmp[i]).append("\n");
            }
            allTxt = sb.toString().trim();
        }
        return allTxt;
    }

    public static String[] PatternVU(String text) {
        text = text.replace(",", "").trim();
        Pattern PATTERN_Value_Unit = Pattern
                .compile("([0-9]*[.]*[0-9]*)(\\ *)([a-zA-Z]*)");
        Matcher matcher = PATTERN_Value_Unit.matcher(text);
        if (matcher.find()) {
            /*
             * String[] array = new String[3]; for (int i = 0; i <
             * matcher.groupCount(); i++) { array[i] = matcher.group(i +
             * 1).trim(); }
             */
            String[] array = new String[2];
            array[0] = matcher.group(1).trim();
            array[1] = matcher.group(3).trim();
            return array;
        }
        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if ((ch < '0' || ch > '9') && ch != '.') {
                return new String[] { "-1", "" };
            }
        }
        return new String[] { text, "" };
    }

    public static String getMessageWhenBlankRowFound(String message) {
        int blankRow = 0;
        String[] values = null;

        if (message != null) {
            if (message.contains("\n")) {
                values = message.trim().split("\n");
            } else {
                values = new String[] { message };
            }
        } else {
            return "";
        }

        if (values == null) {
            return message;
        }

        String value;
        StringBuffer sbMessage = new StringBuffer();
        do {
            value = values[blankRow];
            sbMessage.append(value).append("\n");
            blankRow++;
        } while (blankRow != values.length && !"".equals(value.trim()));

        message = sbMessage.toString().trim();

        return message;
    }

    public static int findRowIndexByMessageLastRow(String message,
                                                   String[] allRows) {
        return findRowIndexByMessageLastRowFrom(message, 1, allRows);
    }

    public static int findRowIndexByMessageLastRowFrom(String message,
                                                       int fromIndex, String[] allRows) {
        int rowIndex = -1;
        if (message == null) {
            return rowIndex;
        }
        if (message.contains("\n")) {
            int messageLength = message.trim().split("\n").length;
            message = message.split("\n")[messageLength - 1].trim();
        }
        for (int i = fromIndex; i < allRows.length; i++) {
            if (pureAscii(allRows[i]).trim().contains(message.trim())) {
                rowIndex = i;
                break;
            }
        }
        return rowIndex;
    }

    public static String getAreaValueWithoutTargetStr(String strTmp,
                                                      String targetStr) {
        if (targetStr.contains(",")) {
            targetStr = targetStr.replace(",", " ");
        }
        String[] str = targetStr.split(" ");
        for (int i = 0; i < str.length; i++) {
            strTmp = strTmp.replaceFirst(str[i], "");
        }
        strTmp = strTmp.replaceFirst(":", "").trim();
        return strTmp;
    }

    public static String cutColon(String oldStr){
        if(oldStr.contains(":")){


            if(oldStr.split(":").length>1 && !oldStr.split(":")[1].isEmpty())
                return oldStr.split(":")[1].trim();
        }
        return oldStr.replace(":", "").trim();

    }


    public static void print(String msg){
        System.out.println("===========================================");
        System.out.println(msg);
        System.out.println("===========================================");
    }
    public static void main(String[] args) {
        System.out.println(PdfUtil.PatternVU("100pkgs   ")[1]);
    }

}
