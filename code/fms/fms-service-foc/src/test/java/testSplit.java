import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2017/3/6.
 */
public class testSplit {
    public static void main(String[] args) {
        String cfpArr="\n" +
                "\n" +
                "plan 8851               zytx to zgsz 320i     35/fifr  01/08/12\n" +
                "nonstop computed 0236z  for etd 0625z  progs  3118uk   b6835    kgs\n" +
                "\n" +
                "shenzhen airlines                       callsign:  zh9828\n" +
                "\n" +
                "------------------------------- ------------------------------------\n" +
                "i certify that this flight is released/dispatched in accordance\n" +
                "with all applicable ccar regulations\n" +
                "\n" +
                "     planner                      sign . . . . . . . . . . .\n" +
                "\n" +
                "     dispatcher                   sign . . . . . . . . . . .\n" +
                "\n" +
                "     pilot in command             sign . . . . . . . . . . .\n" +
                "\n" +
                "------------------------------- ------------------------------------\n" +
                "\n" +
                "block in . . . .      off . . .        ldg . . .   t/o fuel . . .\n" +
                "\n" +
                "block out  . . .      on  . . .        t/o . . .   ldg fuel . . .\n" +
                "\n" +
                "total    . . . .   total  . . .      total . . .  fuel used . . .\n" +
                "\n" +
                "reason for delay . . . . . . . . . . . . . . . . . . . . . . . . .\n" +
                "\n" +
                "------------------------------- ------------------------------------\n" +
                "\n" +
                "          fuel  time  dist arrive takeoff  land   av pld  opnlwt\n" +
                "poa zgsz 010162 03/41 1579  1006z  070940 060778  012789  043310\n" +
                "alt zgkl 002478 01/00 0350  1106z\n" +
                "hld      000000 00/00\n" +
                "res      001801 00/45\n" +
                "xtr      000400 00/12\n" +
                "apu      000130 01/00\n" +
                "txo      000082 00/07\n" +
                "txi      000000 00/00\n" +
                "fob      015053 05/38   trk zytxzgsz-c1\n" +
                "\n" +
                "fuel burn adjustment for 1000kgs incr/decr in tow : 0078kgs/0088kgs\n" +
                "\n" +
                "route avg wind m012   mxsh 03/nikit\n" +
                "\n" +
                "route avg temp m25    flight level  321/wyn   177";
        String mainCompanyRoute=cfpArr.substring(cfpArr.indexOf("fob"),cfpArr.indexOf("route avg"));

        String [] AA=StringUtils.split(mainCompanyRoute,"\n");
        String [] mainCompanyRou=StringUtils.split(AA[0],"  ");

        System.out.print(mainCompanyRou[mainCompanyRou.length-1]);
    }
}
