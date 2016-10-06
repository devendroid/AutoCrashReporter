package com.devs.acr;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


public class ErrorReporterActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erroe_reporter);

        this.setFinishOnTouchOutside(false);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        init();
    }

    private void init() {
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        TextView tvReport = (TextView) findViewById(R.id.tv_report);
        tvReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            finish();

        }
        else if (i == R.id.tv_report) {
            AutoErrorReporter.getInstance().checkErrorAndSendMail(this);
            finish();
        }
    }
}


