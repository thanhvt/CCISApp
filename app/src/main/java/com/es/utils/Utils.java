package com.es.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.es.ccisapp.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {
    public static void startFragment(FragmentManager manager, Fragment fragment) {
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_body, fragment).commit();
    }

    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    private static final String DECIMAL_FORMAT = "###,###,###.#";

    public static String formatValue(String strIn) {
        try {
            BigDecimal value = new BigDecimal(strIn);
            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            formatSymbols.setDecimalSeparator('.');
            formatSymbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT, formatSymbols);
            return formatter.format(value);
        } catch (Exception e) {
            return strIn;
        }

    }

    public static int CalculateTotalPartialMonth(Date d2, Date d1) {
        try {
            Calendar c2 = Calendar.getInstance();
            c2.setTime(d2);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);
            double monthsApart = Math.abs(12 * (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH)) - 1;
            int daysInMonth1 = c1.getActualMaximum(Calendar.DATE);
            ;// DateTime.DaysInMonth(d1.Year, d1.Month);
            int daysInMonth2 = c2.getActualMaximum(Calendar.DATE);
            ; // DateTime.DaysInMonth(d2.Year, d2.Month);

            double dayPercentage = ((double) (daysInMonth1 - c1.get(Calendar.DAY_OF_MONTH) + 1) / (double) daysInMonth1) + (c2.get(Calendar.DAY_OF_MONTH) / daysInMonth2);
            return (int) Math.round((monthsApart + dayPercentage) < 1 ? 1 : (monthsApart + dayPercentage));
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String strX = pattern.matcher(temp).replaceAll("");
        strX = strX.replaceAll("Đ", "D").replace("đ", "");
        return strX;
    }

    public static Date parseDate(String sDate) {
        Date dateReturn = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                dateReturn = df.parse(sDate);
            } catch (Exception ex) {
                df = new SimpleDateFormat("MMM d,yyyy HH:mm:ss aaa");
                dateReturn = df.parse(sDate);
            }
        } catch (Exception ex) {
            return null;
        }
        return dateReturn;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String doubleFormatter(double number) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);

        NumberFormat formatter = new DecimalFormat("#0.00");

        String formattedNumber = formatter.format(number);

        return formattedNumber;

    }

    public static boolean isOnline(Context m) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) m.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String formatNumberForRead(double number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        String temp = nf.format(number);
        String strReturn = "";
        int slen = temp.length();
        for (int i = 0; i < slen; i++) {
            if (String.valueOf(temp.charAt(i)).equals("."))
                break;
            else if (Character.isDigit(temp.charAt(i))) {
                strReturn += String.valueOf(temp.charAt(i));
            }
        }
        return strReturn;

    }

    public static String numberToString(double number) {
        String sNumber = formatNumberForRead(number);
        // Tao mot bien tra ve
        String sReturn = "";
        // Tim chieu dai cua chuoi
        int iLen = sNumber.length();
        // Lat nguoc chuoi nay lai
        String sNumber1 = "";
        for (int i = iLen - 1; i >= 0; i--) {
            sNumber1 += sNumber.charAt(i);
        }
        // Tao mot vong lap de doc so
        // Tao mot bien nho vi tri cua sNumber
        int iRe = 0;
        do {
            // Tao mot bien cat tam
            String sCut = "";
            if (iLen > 3) {
                sCut = sNumber1.substring((iRe * 3), (iRe * 3) + 3);
                sReturn = Read(sCut, iRe) + sReturn;
                iRe++;
                iLen -= 3;
            } else {
                sCut = sNumber1.substring((iRe * 3), (iRe * 3) + iLen);
                sReturn = Read(sCut, iRe) + sReturn;
                break;
            }
        } while (true);
        if (sReturn.length() > 1) {
            sReturn = sReturn.substring(0, 1).toUpperCase() + sReturn.substring(1);
        }
        sReturn = sReturn + "dong";
        return sReturn;
    }

    // Khoi tao ham Read
    public static String Read(String sNumber, int iPo) {
        // Tao mot bien tra ve
        String sReturn = "";
        // Tao mot bien so
        String sPo[] = {"", "ngan" + " ",
                "trieu" + " ", "ty" + " "};
        String sSo[] = {"khong" + " ", "mot" + " ",
                "hai" + " ", "ba" + " ",
                "bon" + " ", "nam" + " ",
                "sau" + " ", "bay" + " ",
                "tam" + " ", "chin" + " "};
        String sDonvi[] = {"", "muoi" + " ",
                "tram" + " "};
        // Tim chieu dai cua chuoi
        int iLen = sNumber.length();
        // Tao mot bien nho vi tri doc
        int iRe = 0;
        // Tao mot vong lap de doc so
        for (int i = 0; i < iLen; i++) {
            String sTemp = "" + sNumber.charAt(i);
            int iTemp = Integer.parseInt(sTemp);
            // Tao mot bien ket qua
            String sRead = "";
            // Kiem tra xem so nhan vao co = 0 hay ko
            if (iTemp == 0) {
                switch (iRe) {
                    case 0:
                        break;// Khong lam gi ca
                    case 1: {
                        if (Integer.parseInt("" + sNumber.charAt(0)) != 0) {
                            sRead = "le" + " ";
                        }
                        break;
                    }
                    case 2: {
                        if (Integer.parseInt("" + sNumber.charAt(0)) != 0
                                && Integer.parseInt("" + sNumber.charAt(1)) != 0) {
                            sRead = "khong tram" + " ";
                        }
                        break;
                    }
                }
            } else if (iTemp == 1) {
                switch (iRe) {
                    case 1:
                        sRead = "muoi" + " ";
                        break;
                    default:
                        sRead = "mot" + " " + sDonvi[iRe];
                        break;
                }
            } else if (iTemp == 5) {
                switch (iRe) {
                    case 0: {
                        if (sNumber.length() <= 1) {
                            sRead = "nam" + " ";
                        } else if (Integer.parseInt("" + sNumber.charAt(1)) != 0) {
                            sRead = "lam" + " ";
                        } else
                            sRead = "nam" + " ";
                        break;
                    }
                    default:
                        sRead = sSo[iTemp] + sDonvi[iRe];
                }
            } else {
                sRead = sSo[iTemp] + sDonvi[iRe];
            }

            sReturn = sRead + sReturn;
            iRe++;
        }
        if (sReturn.length() > 0) {
            sReturn += sPo[iPo];
        }

        return sReturn;
    }


}
