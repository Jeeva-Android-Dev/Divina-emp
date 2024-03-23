package com.mazenet.mani.gurugubera.Activities;

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.widget.Button
import com.mazenet.mani.gurugubera.Fragments.Collection.ViewReceiptPreview
import com.mazenet.mani.gurugubera.Model.successmsgmodel
import com.mazenet.mani.gurugubera.R
import com.mazenet.mani.gurugubera.Retrofit.ICallService
import com.mazenet.mani.gurugubera.Retrofit.RetrofitBuilder.buildservice
import com.mazenet.mani.gurugubera.Utilities.BaseActivity
import com.mazenet.mani.gurugubera.Utilities.BaseUtils
import com.mazenet.mani.gurugubera.Utilities.Constants.db
import com.mazenet.mani.gurugubera.Utilities.Constants.loggeduser
import com.mazenet.mani.gurugubera.Utilities.Constants.tenantid
import com.softland.palmtecandro.palmtecandro
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * created by monica This  activity is for printing  on  palmtech devive
 */

class PrintActivity1 : BaseActivity() {


    private var receivedmsg = ""
    private var cmdforcharge: Byte = 0
    private var prevCmd = ""
    private var tosnd: IntArray= IntArray(16 * 1024)
    private var count = 0
    private val mSendButton: Button? = null
    private var btncancel:Button?= null
    private var btnprin:Button?= null
    private var mPrint: Button? = null
    var username = ""
    var Cusname: String? = ""
    var receivedamount:String?=""
    var Groupname:String?=""
    var ticketno:String?=""
    var paymode:String?=""
    var penaltyamnt:String?=""
    var totaldue:String?=""
    var bonusamnt:String?=""
    var due:String?=""
    var cheno:String?=""
    var chebank:String?=""
    var chedate:String?=""
    var chequebranch:String?=""
    var transactionno:String?=""
    var transactiondate:String?=""
    var subid:String?=""
    var mobile:String?=""
    var recptno:String?=""
    var recptdate:String?=""
    var isprinted:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = getPrefsString("username", "")

        setContentView(R.layout.activity_test)
        btncancel = findViewById<android.view.View?>(R.id.closeb) as Button?
        btncancel?.setOnClickListener {
            finishscreen1()
        }
        palmtecandro.jnidevOpen(115200)
        count = 0;
        InitUIControl()
        //fininvoice = "vjv-8";
        //fininvoice = "vjv-8";
        try {
            val it = intent
            Cusname = it.getStringExtra("Cusname")
            receivedamount = it.getStringExtra("receivedamount")
            Groupname = it.getStringExtra("groupname")
            ticketno = it.getStringExtra("ticketno")
            paymode = it.getStringExtra("paymentmode")
            penaltyamnt = it.getStringExtra("penalty")
            totaldue = it.getStringExtra("totaldue")
            bonusamnt = it.getStringExtra("bonus")
            due = it.getStringExtra("installmentno")
            cheno = it.getStringExtra("chequeno")
            chebank = it.getStringExtra("chequebank")
            chedate = it.getStringExtra("chequedate")
            chequebranch = it.getStringExtra("chequebranch")
            transactionno = it.getStringExtra("transactionno")
            transactiondate = it.getStringExtra("transactiondate")
            subid = it.getStringExtra("customerid")
            mobile = it.getStringExtra("customermobile")
            recptno = it.getStringExtra("recptno")
            recptdate = it.getStringExtra("recptdate")
            isprinted = it.getStringExtra("isprinted")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    private fun InitUIControl() {
        mPrint = findViewById<View>(R.id.pp) as Button
        //  mPrint!!.setOnClickListener(mBtnPrintOnClickListener)

    }

    override fun onStart() {
        super.onStart()
        palmtecandro.jnidevOpen(115200)
    }

    override fun onStop() {
        super.onStop()
        palmtecandro.jnidevClose()
    }

    override fun onDestroy() {
        super.onDestroy()
        palmtecandro.jnidevClose()
    }

    private fun setCmd(cmd: Byte) {
        tosnd[count] = cmd.toInt()
        count++
    }

    private fun setCmd(m: ByteArray) {
        for (j in m.indices) {
            tosnd[count] = m[j].toInt()
            count++
        }
    }

    private fun sendMessageToPrinter(message: String) {
        receivedmsg = message
        var cmd = 0x10.toByte()
        count = 0
        if (message.length > 0) {
            val strArray = message.split("<").toTypedArray()
            try {
                for (i in strArray.indices) {
                    val mstr = "<" + strArray[i]
                    val pattern: Pattern = Pattern.compile("<(.*?)>")
                    val matcher: Matcher = pattern.matcher(mstr)
                    var strPrintArray: String
                    var hex: String
                    if (matcher.find()) {
                        strPrintArray = mstr.replace("<" + matcher.group(1).toString() + ">", "")
                        try {
                            if (matcher.group(1)?.equals("0x09") == true) {
                                Log.e("inside 09 match: ", "asdad")
                                // Normal
                                //cmd = ( byte ) 0x09;
                                val m = ByteArray(2)
                                val m2 = ByteArray(3)
                                /* m[0] = (byte)0x1b;
                                        m[1] = (byte)0x74;
                                        m[2] = (byte)0x00;*/m[0] = 0x1b.toByte()
                                m[1] = 0x40.toByte()
                                setCmd(m)
                                //Thread.sleep(1050);
                                m2[0] = 0x1b.toByte()
                                m2[1] = 0x21.toByte()
                                m2[2] = 0x00.toByte()
                                setCmd(m2)
                            } else if (matcher.group(1)?.equals("0x11") == true) {
                                // Double Width
                                cmd = 0x11.toByte()
                                // Thread.sleep(1050);
                                if (i == 1) {
                                    //   Thread.sleep( 1050 );
                                }
                                // Log.e( "MAINCHAT", "<0x11>" );
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x10")) {
                                // Double Height
                                cmd = 0x10.toByte()
                                // Log.e( "MAINCHAT", "<0x10>" );

                                // Thread.sleep(1050);
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x12")) {
                                // Double
                                cmd = 0x12.toByte()
                                // Thread.sleep(1050);
                                // Log.e( "MAINCHAT", "<0x12>" );
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x13")) {
                                // LandScape for 80 length
                                //  byte[] m = new byte[ 5 ];
                                val m = ByteArray(2)
                                val m2 = ByteArray(3)
                                m2[0] = 0x1b.toByte()
                                m2[1] = 0x13.toByte()
                                m2[2] = 0x50.toByte()
                                setCmd(m2)
                            } else if (matcher.group(1).equals("0x14")) {
                                // LandScape for 140 length
                                val m = ByteArray(2)
                                val m2 = ByteArray(3)
                                m2[0] = 0x1b.toByte()
                                m2[1] = 0x13.toByte()
                                m2[2] = 0x8c.toByte()
                                setCmd(m2)
                            } else if (matcher.group(1).equals("0xpaper")) {
                                val m = ByteArray(2)
                                val m2 = ByteArray(3)
                                m2[0] = 0x1b.toByte()
                                m2[1] = 0x40.toByte()
                                m2[1] = 0x0A.toByte()
                                setCmd(m2)
                            } else if (matcher.group(1).equals("0x40")) {
                                val m = ByteArray(2)
                                m[0] = 0x1b.toByte()
                                m[1] = 0x40.toByte()
                                setCmd(m)
                                //Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x0B")) {
                                // BarCode
                                cmd = 0x0B.toByte()
                                // Thread.sleep(1050);
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x0D")) {
                                // paper feed
                                cmd = 0x0D.toByte()
                                // Thread.sleep(1050);
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x81")) {
                                // Graphics
                                cmd = 0x81.toByte()
                                // Thread.sleep(1050);
                                setCmd(cmd)

                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x0A")) {
                                // New Line
                                cmd = 0x0A.toByte()
                                // Thread.sleep(1050);
                                setCmd(cmd)
                            } else if (matcher.group(1).equals("0x05")) {
                                // Marati Normal
                                cmd = 0x05.toByte()
                                cmdforcharge = 0x05.toByte()

                                // Thread.sleep(1050);
                                setCmd(cmd)
                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x0E")) {
                                // New Line
                                cmd = 0x0E.toByte()

                                // Thread.sleep(1050);
                                setCmd(cmd)
                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x80")) {
                                // Graphics
                                cmd = 0x80.toByte()
                                // Log.e( "simpleimage", "<0x80>" );
                                // Thread.sleep(1050);
                                setCmd(cmd)

                                // Log.e("simple","  (matcher.group(1).equals("
                                // + strPrintArray );
                                // Thread.sleep(1050);
                            } else if (matcher.group(1).equals("0x82")) {
                                // New Line
                                cmd = 0x82.toByte()
                                val m = ByteArray(2)
                                m[0] = cmd
                                m[1] = cmd
                                println("if cmd == 82 ")
                                setCmd(cmd)
                                // Thread.sleep(1500);
                                // Log.e(strPrintArray,"   length()   "+strPrintArray.length()+"   cmd  "+cmd);
                            } else if (matcher.group(1).equals("0x16")) {
                                // New Line
                                cmd = 0x16.toByte()
                                setCmd(cmd)
                                println("if cmd == 16 ")
                                // Thread.sleep(1500);
                                // Log.e(strPrintArray,"   length()   "+strPrintArray.length()+"   cmd  "+cmd);
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        prevCmd = matcher.group(1)
                        val Strlength = strPrintArray.length
                        val _data = strPrintArray.toByteArray()
                        for (j in 0 until Strlength) {
                            tosnd[count] = _data[j].toInt()
                            count++
                        }
                    }else{
                        // Send new line command
                        cmd = 0x0A.toByte()
                        setCmd(cmd)
                        val Strlength = receivedmsg.length
                        val _data = receivedmsg.toByteArray()
                        for (j in 0 until Strlength) {
                            tosnd[count] = _data[j].toInt()
                            count++
                        }
                        setCmd(cmd)
                        setCmd(cmd)
                    }
                }
                val splitLimit = 4090
                if (count > splitLimit) {
                    val iCount = count / splitLimit
                    val remain = count - iCount * splitLimit
                    var totCount = 0
                    val _dataArrSplitRemain = IntArray(remain)
                    for (i in 0 until remain) {
                        val icountNew = iCount * splitLimit + i
                        _dataArrSplitRemain[i] = tosnd[icountNew]
                        //   System.out.println("totCount"+totCount + " " + i + " " + _dataArrSplitRemain[i]);
                        Log.e(
                            "totCount",
                            "totCount" + totCount + " " + i + " " + _dataArrSplitRemain[i]
                        )
                        totCount++
                    }
                    for (i in 0 until iCount) {
                        val _dataArrSplit = IntArray(splitLimit)
                        for (j in 0 until splitLimit) {
                            val icountNew = i * splitLimit + j
                            _dataArrSplit[j] = tosnd[icountNew]
                            //    System.out.println("totCount"+totCount + " " + j + " " + _dataArrSplit[j]);
                            Log.e("totCount", "totCount$totCount $j $icountNew")
                            totCount++
                        }
                        palmtecandro.jnidevDataWrite(_dataArrSplit, splitLimit)
                    }
                    Thread.currentThread()
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    palmtecandro.jnidevDataWrite(_dataArrSplitRemain, remain)
                    //  palmtecandro.jnidevDataWrite(tosnd, iCount);
                } else palmtecandro.jnidevDataWrite(tosnd, count)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                // Thread.sleep(1500);
                // ( ( Activity ) PrintService.this ).finish();
                mSendButton!!.text = "Reprint"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    val ESC: Byte = 0x1B

    fun PrintData(view: View) {
        //Print config "mode"
        //Print config "mode"
        val cc = byteArrayOf(0x1B, 0x21, 0x00) // 0- normal size text
        // val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold with medium text


        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        val bb = byteArrayOf(0x1B, 0x21, 0x10) // 1- Double height

        val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- Double width

        val bb3 = byteArrayOf(0x1B, 0x21, 0x30) // 3- Double

        val printData: String = "$ESC<0x09>"
        var full_address = StringBuilder()
        if (isprinted.equals("no", ignoreCase = true)) {
        } else {
            //palmtecandro.jnidevDataByteWrite(bb,0x00);
            //sendMessageToPrinter("[ Duplicate Copy ]")
            //PrinterCommands.LF;
        }
        palmtecandro.jnidevDataByteWrite(bb,0x00);
       // sendMessageToPrinter(getPrefsString("teanant_name", "").trimIndent()+"\n"+"--------------------------------")
        sendMessageToPrinter("DIVINA CHIT FUNDS PVT LTD \n"+"--------------------------------\n"+
        "KANJIRACODE-629155\n"+"TAMILNADU.\n"+"TEL: 04651-222699" )
        PrinterCommands.LF;
      /*  val address1 = getPrefsString("tenant_address1", "")
        val address2 = getPrefsString("tenant_address2", "")
        val tenant_state = getPrefsString("tenant_state", "")
        val tenant_phone = getPrefsString("tenant_phone", "")
        val tenant_mobile = getPrefsString("tenant_mobile", "")
        if (address1.isEmpty()) {
            if (address2.isEmpty()) {
            } else {
                palmtecandro.jnidevDataByteWrite(bb,0x00);
                sendMessageToPrinter("KANJIRACODE-629155")
                PrinterCommands.LF;
            }
        } else {
            palmtecandro.jnidevDataByteWrite(bb,0x00);
            sendMessageToPrinter("KANJIRACODE-629155")
            PrinterCommands.LF;

        }
        palmtecandro.jnidevDataByteWrite(bb,0x00);
        sendMessageToPrinter("TAMILNADU")
        PrinterCommands.LF;
        if (tenant_phone.isEmpty()) {
            if (tenant_mobile.isEmpty()) {
            } else {
                palmtecandro.jnidevDataByteWrite(bb,0x00);
                sendMessageToPrinter("TEL: 04651-222699")
                PrinterCommands.LF;
            }
        } else {
            palmtecandro.jnidevDataByteWrite(bb,0x00);
            sendMessageToPrinter("TEL: 04651-222699")
            PrinterCommands.LF;
        } */
        palmtecandro.jnidevDataByteWrite(bb,0x00);
        sendMessageToPrinter("Rec No - $recptno \n"+"Date - $recptdate \n"+"Customer -$Cusname\n\n"
                + "Group-$Groupname\n" + "Paid Amount-$receivedamount\n" +
                "--------------------------------"+"Thank You, Welcome Again \n" )
        PrinterCommands.LF;
        palmtecandro.jnidevDataRead()
        if (paymode.equals("Cheque", ignoreCase = true)){
            palmtecandro.jnidevDataByteWrite(bb,0x00);
            sendMessageToPrinter("Cheque No. :$cheno\n Cheque Bank:$chebank \n Cheque bank branch  : $chequebranch \n Cheque Date  : $chedate" );
            PrinterCommands.LF;
        } else if (paymode.equals("D.D", ignoreCase = true)){
            palmtecandro.jnidevDataByteWrite(bb,0x00);
            sendMessageToPrinter("D.D No. : $cheno \n D.D Bank. : $chebank \n D.D bank branch   : $chequebranch \n D.D Date  : $chedate");
            PrinterCommands.LF;
        }else if (paymode.equals("RTGS/NEFT", ignoreCase = true) || paymode.equals(
                "Card",
                ignoreCase = true
            )){
            palmtecandro.jnidevDataByteWrite(bb,0x00);
            sendMessageToPrinter(" Transaction No : $transactionno \n Transaction Date  : $transactiondate ");
            PrinterCommands.LF;
        }else{
        }
        if (paymode.equals("cheque", ignoreCase = true)) {
            sendMessageToPrinter("*Subject to Realization")
            palmtecandro.jnidevDataRead()
        }
       // palmtecandro.jnidevDataByteWrite(bb,0x00);
       // sendMessageToPrinter("-------------------------------- Group:$Groupname\n Ticket No :$ticketno \n Received Amount:$receivedamount\n Payment Mode:$paymode")
       // sendMessageToPrinter("--------------------------------\nThank You, Welcome Again \n")
       // PrinterCommands.LF;
        if (getPrefsBoolean("isonline", true)) {
            recptno?.let { update_printed(it) }
        } else {
            recptno?.let { BaseUtils.offlinedb.update_printed_receipts(it) }
        }
        finish()
        val intentnext: Intent
        intentnext = Intent(this@PrintActivity1, ViewReceiptPreview::class.java)
        intentnext.putExtra("PRINTDATA", printData + ESC)
        intentnext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(intentnext)
        val text= findViewById<TextInputEditText>(R.id.text_sample).text?.trim();
        if (text != null) {
            if(text.contains("")){
                sendMessageToPrinter("")
            }else{
                sendMessageToPrinter(text.toString())
            }
        }

    }
    fun update_printed(recieptno:String) {
        val service = buildservice(ICallService::class.java)
        val params: HashMap<String, String> = HashMap<String, String>()
        params["tenant_id"] =
            getPrefsString(tenantid, "")
        params["user_id"] =
            getPrefsString(loggeduser, "")
        params["receipt_no"] = recieptno
        params["db"] = getPrefsString(db, "")
        val RequestCall = service.update_printed_status(params)

        RequestCall.enqueue(object : retrofit2.Callback<successmsgmodel>
        {
            override fun onResponse(call: Call<successmsgmodel>, response: Response<successmsgmodel>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        if (response.body()!!.getStatus().equals("Success", ignoreCase = true)) {
                        } else {
                            toast(response.body()!!.getMsg()!!)
                        }
                    } else {
                        println("no show")
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<successmsgmodel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        });

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    fun closeApp(view: View) {

        finish()
    }
    fun finishscreen1() {
        val builder = android.app.AlertDialog.Builder(
            this@PrintActivity1,
            com.mazenet.mani.gurugubera.R.style.MyErrorDialogTheme
        )
        builder.setCancelable(false)
        builder.setTitle("Caution")
            .setIcon(resources.getDrawable(R.drawable.ic_info_red))
            .setMessage("Do you want exit the Print?")
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                supportFinishAfterTransition()
            }
        builder.create().show()
    }

}

