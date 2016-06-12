package com.assignment12.cs.softstudio_assignment12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** Init Variable for IP page **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";

    /** Init Variable for Page calc **/
    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    /** Init Variable for Page 2 **/
    TextView textResult;

    Button return_button;

    /** Init Variable **/
    String oper = "";
    Thread thread;
    String resultString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_page);
        inputIP = (EditText)findViewById(R.id.inputIP);
        ipSend = (Button)findViewById(R.id.ipSend);

        ipSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Func() for setup page 1 **/
                ipAdd = inputIP.getText().toString();
                jumpToMainLayout();
            }
        });
    }

    /*private void sendStrToServer(String sendMsg)
    {
        this.writer.println(sendMsg);
        this.writer.flush();
    }*/

    public void jumpToMainLayout() {
        //TODO: Change layout to activity_main
        setContentView(R.layout.activity_main);

        inputNumTxt1 = (EditText) findViewById(R.id.edNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.edNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString()) || TextUtils.isEmpty(inputNumTxt2.getText().toString()))
        {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        switch (v.getId())
        {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }

        Log.d("debug","ANS "+result);
        Log.d("Client","Client Send");
        Thread t = new thread();
        t.start();
        resultString = (num1 + " " + oper + " " + num2 + " = " + result);
        jumpToResultLayout(resultString);

    }

    public void jumpToResultLayout(String resultStr){
        setContentView(R.layout.result_page);


        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null) {

            textResult.setText(resultStr);
        }

        if (return_button != null) {

            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    jumpToMainLayout();
                }

            });
        }
    }

    class thread extends Thread
    {
        private PrintWriter writer;
        public void run()
        {
            try{
                System.out.println("Client: Waiting to connect...");
                int serverPort = 9527;

                // Create socket connect server
                Socket socket = new Socket(ipAdd, serverPort);
                System.out.println("Connected!");

                // Create stream communicate with server
                OutputStream out = socket.getOutputStream();
                String strToSend = resultString;

                /*byte[] sendStrByte = new byte[1024];
                System.arraycopy(strToSend.getBytes(), 0, sendStrByte, 0, strToSend.length());
                out.write(sendStrByte);*/

                writer = new PrintWriter(out);
                writer.println(resultString);
                writer.flush();

            }catch (Exception e){
                //System.out.println("Error" + e.getMessage());
            }
        }
    }


}
