package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.Pay.CompleteOrder_PayActivity;
import com.example.kioskmainpage.Adapter.ReceiptMenuAdapter;
import com.example.kioskmainpage.CardReceiptInfo;
import com.example.kioskmainpage.KakaoPayReceiptInfo;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class ReceiptPopupActivity extends AppCompatActivity implements View.OnClickListener{

    TextView store_name,biz_num,address,telephone,approval_date, approval_no;
    TextView tid;//단말기번호
    TextView credit_card_no, credit_card_company, installment_month;
    TextView order_num_textview;
    TextView payment_price_textview1, payment_price_textview2,surtax_price;
    TextView price_sum_textview1,price_sum_textview2;
    TextView pay_way;//결제방법
    TextView paper_num;//전표번호
    //    ListView listView;
//    ReceiptMenuAdapter adapter;
    ArrayList<SelectedMenu> selectedMenus;
    Myapplication app;
    CardReceiptInfo cardReceiptInfo;
    KakaoPayReceiptInfo kakaoPayReceiptInfo;

    LinearLayout credit_Card_paper;
    String phonenumber;

    int order_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_popup);
        //  countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Intent dataforloading = getIntent() ;
        phonenumber  = dataforloading.getStringExtra("phonenumber"); //21 쿠폰 22 영수증

        Button btnFinish_receipt = (Button) findViewById(R.id.btnFinish_receipt);
        btnFinish_receipt.setOnClickListener(this);
        Button btnCancel_receipt = (Button) findViewById(R.id.btnCancel_receipt);
        btnCancel_receipt.setOnClickListener(this);

        store_name = (TextView) findViewById(R.id.store_name); //상호명
        pay_way = (TextView) findViewById(R.id.pay_way);//결제방법
        biz_num = (TextView) findViewById(R.id.biz_num);//사업자 번호
        address = (TextView) findViewById(R.id.address); //주소
        telephone = (TextView) findViewById(R.id.telephone_number);//전화번호
        approval_date = (TextView) findViewById(R.id.approval_date_receipt);//승인날짜
        order_num_textview = (TextView) findViewById(R.id.order_num);//주문번호

        surtax_price = (TextView) findViewById(R.id.surtax_price);//부가세금액
        payment_price_textview1 = (TextView) findViewById(R.id.payment_price_1);//결제금액

        //price_sum_textview1 = (TextView) findViewById(R.id.price_sum_1);//단가
        price_sum_textview2 = (TextView) findViewById(R.id.price_sum_2);//합계

        credit_card_no = (TextView) findViewById(R.id.credit_card_num);//카드번호
        credit_card_company = (TextView) findViewById(R.id.credit_card_company);//카드회사
        payment_price_textview2 = (TextView) findViewById(R.id.payment_price_2);//결제금액
        installment_month = (TextView) findViewById(R.id.monthly_installment_plan);//할부개월
        approval_no = (TextView) findViewById(R.id.approve_num);//승인번호
        tid = (TextView) findViewById(R.id.terminal_num);//단말기번호

        credit_Card_paper = (LinearLayout) findViewById(R.id.credit_card_paper);//캡쳐할 영수증 레이아웃
        int sum=0;
        int sum2=0;
        app=(Myapplication) getApplication();
        order_num = app.getOrderNum();
        cardReceiptInfo =app.getCurrentCardReceiptInfo();
        selectedMenus = app.getadapter().getselectedMenus();
        //        adapter = new ReceiptMenuAdapter();
//        adapter.setSelectedItemList(selectedMenus);
//        listView = (ListView)findViewById(R.id.item_list);
        for(int i=0;i<selectedMenus.size();i++)
        {
            SelectedMenu selectedMenu = selectedMenus.get(i);
            System.out.println("영수증 메뉴 : "+selectedMenu.getMenu_name() + " "+selectedMenu.getPrice_sum());
            sum += selectedMenu.getPrice_sum();//총합계
            sum2 += selectedMenu.getPrice_one();//단가합
        }

        //        listView.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(listView);//list view 높이 아이템개수에따라 동적조절 함수

        LinearLayout L = (LinearLayout)findViewById(R.id.item_list2);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i = 0;i<selectedMenus.size();i++)
        {
            LinearLayout L_item = (LinearLayout) inflater.inflate(R.layout.receipt_item, L, false);
            TextView T0 = (TextView)((TextView)L_item.getChildAt(0));
            TextView T1 = (TextView)((TextView)L_item.getChildAt(1));
            TextView T2 = (TextView)((TextView)L_item.getChildAt(2));
            TextView T3 = (TextView)((TextView)L_item.getChildAt(3));
            String price,cnt,price_sum;
            price = NumberFormat.getInstance().format(selectedMenus.get(i).getPrice_one());
            cnt = NumberFormat.getInstance().format(selectedMenus.get(i).getCnt());
            price_sum = NumberFormat.getInstance().format(selectedMenus.get(i).getPrice_sum());
            T0.setText(selectedMenus.get(i).getMenu_name());
            T1.setText(price+"");
            T2.setText(cnt+"");
            T3.setText(price_sum+"");
//            T0.setText(selectedMenus.get(i).getMenu_name());
//            T1.setText(selectedMenus.get(i).getPrice_one()+"");
//            T2.setText(selectedMenus.get(i).getCnt()+"");
//            T3.setText(selectedMenus.get(i).getPrice_sum()+"");
            L.addView(L_item);
        }


        String pay_type = app.getCurrentPayType();
        switch (pay_type){
            case "Card" :
                pay_way.setText("신용카드");
                break;
            case "SAMSUNGPAY" :
                pay_way.setText("삼성페이");
                break;
            case "KaKao" :
                pay_way.setText("카카오페이");
                break;
            case "Zero" :
                pay_way.setText("제로페이");
                break;
            default:
                pay_way.setText("비정상적인접근");
                break;
        }
        if((pay_type.equals("Card")|pay_type.equals("SAMSUNGPAY")))//card로 결제할때만 세팅
            settingForCard(pay_type);
        else /*TODO : 나중에 카카오페이나 제로 페이도 매장 정보 받아와서 출력 해줘야함, 숫자에 쉼표(,)표시구현해줘야함 */
        {

            payment_price_textview1.setText(sum+"");//최종결제금액
            if(pay_type.equals("KaKao"))
            {
                kakaoPayReceiptInfo=app.getCurrentKakaoPayReceiptInfo();
                settingForKakaoPay();
            }
            else if(pay_type.equals("Zero"))
                order_num_textview.setText(order_num+"");//주문번호
            //price_sum_textview1.setText(sum2+"");//단가합
            price_sum_textview2 .setText(sum+"");//총합계
            payment_price_textview2.setText(sum+"");//결제금액
            surtax_price.setText((int)(sum * 0.1) + "");//부가세금액
            approval_date.setText(makeApprovalDate());//승인 날짜
        }
        Myapplication myapp=(Myapplication) getApplication();
    }
    public String dayToKorean(int day) {

        if (day==2)
            return "월";
        else if (day==3)
            return "화";
        else if (day==4)
            return "수";
        else if (day==5)
            return "목";
        else if (day==6)
            return "금";
        else if (day==7)
            return "토";
        else if (day==1)
            return "일";
        else
            return "";
    }

    //승인 날짜 포맷에 맞게 바꿔줌(카카오페이,제로페이용)
    public String makeApprovalDate()
    {
        String result = "";

        try
        {

            TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
            Calendar calendar = Calendar.getInstance(timeZone);

            String year = String.valueOf(calendar.get(Calendar.YEAR));//년
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);//월
            String date2 = String.valueOf(calendar.get(Calendar.DATE));//날짜
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));//시간
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));;//분
            String day = dayToKorean(calendar.get(Calendar.DAY_OF_WEEK));//요일

            if(Integer.parseInt(month) < 10)
                month = "0"+month;
            if(Integer.parseInt(date2) < 10)
                date2 = "0"+date2;
            if(Integer.parseInt(hour) < 10)
                hour = "0"+hour;
            if(Integer.parseInt(minute) < 10)
                minute = "0"+minute;
            result = year+"-"+month+"-"+date2+" "+day+" "+hour+":"+minute;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
    //승인 날짜 포맷에 맞게 바꿔줌(카드결제용)
    public String makeApprovalDate(String original)
    {
        String result = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date;
        try
        {
            date = format.parse(original);
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
            Calendar calendar = Calendar.getInstance(timeZone);
            calendar.setTime(date);
            String year = String.valueOf(calendar.get(Calendar.YEAR));//년
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);//월
            String date2 = String.valueOf(calendar.get(Calendar.DATE));//날짜
            String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));//시간
            String minute = String.valueOf(calendar.get(Calendar.MINUTE));;//분
            String day = dayToKorean(calendar.get(Calendar.DAY_OF_WEEK));//요일

            if(Integer.parseInt(month) < 10)
                month = "0"+month;
            if(Integer.parseInt(date2) < 10)
                date2 = "0"+date2;
            if(Integer.parseInt(hour) < 10)
                hour = "0"+hour;
            if(Integer.parseInt(minute) < 10)
                minute = "0"+minute;
            result = year+"-"+month+"-"+date2+" "+day+" "+hour+":"+minute;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
    public void settingForKakaoPay()//카카오페이
    {
        /*기기에서
        store_name.setText(cardReceiptInfo.getOrgRegName());//상호
        biz_num.setText(cardReceiptInfo.getOrgNo());//사업자번호
        address.setText(cardReceiptInfo.getOrgAddress());//주소
        telephone.setText(cardReceiptInfo.getOrgTel());//전화번호
        */
        //String date = makeApprovalDate(cardReceiptInfo.getApprovalDate());
        approval_date.setText(kakaoPayReceiptInfo.getR10());//승인날짜
        order_num_textview.setText(order_num+"");//주문번호

        //   surtax_price.setText(cardReceiptInfo.getVatVal()+"");//부가세금액
        //  payment_price_textview1.setText(cardReceiptInfo.getSumVal()+"");//최종결제금액

//        price_sum_textview1.setText(cardReceiptInfo.getSumVal()+"");
        //      price_sum_textview2 .setText(cardReceiptInfo.getSumVal()+"");

        credit_card_no.setText(kakaoPayReceiptInfo.getR22());//카드번호
        credit_card_company.setText(kakaoPayReceiptInfo.getR16());//카드회사
        //payment_price_textview2.setText(cardReceiptInfo.getSumVal()+"");//결제금액
        installment_month.setText("일시불");
        approval_no.setText(kakaoPayReceiptInfo.getR12());//승인번호
        paper_num.setText(kakaoPayReceiptInfo.getR03()+kakaoPayReceiptInfo.getR04());//전표번호
        tid.setText(kakaoPayReceiptInfo.getR06());//단말번호

        credit_Card_paper.setVisibility(View.VISIBLE);//카카오페이일땐 전표 보이게 함
    }
    public void settingForCard(String type)
    {
//        store_name.setText(cardReceiptInfo.getOrgRegName());//상호
        biz_num.setText(cardReceiptInfo.getOrgNo());//사업자번호
        address.setText(cardReceiptInfo.getOrgAddress());//주소
        telephone.setText(cardReceiptInfo.getOrgTel());//전화번호

        String date = makeApprovalDate(cardReceiptInfo.getApprovalDate());
        approval_date.setText(date);//승인날짜
        order_num_textview.setText(order_num+"");//주문번호

        surtax_price.setText(NumberFormat.getInstance().format(cardReceiptInfo.getVatVal())+"");//부가세금액
        payment_price_textview1.setText(NumberFormat.getInstance().format(cardReceiptInfo.getSumVal())+"");//최종결제금액

//        price_sum_textview1.setText(NumberFormat.getInstance().format(cardReceiptInfo.getSumVal())+"");//단가합
        price_sum_textview2 .setText(NumberFormat.getInstance().format(cardReceiptInfo.getSumVal())+"");//총합계

        credit_card_no.setText(cardReceiptInfo.getCardNo());//카드번호
        credit_card_company.setText(cardReceiptInfo.getIssuerName());//카드회사
        payment_price_textview2.setText(NumberFormat.getInstance().format(cardReceiptInfo.getSumVal())+"");//결제금액
        if(cardReceiptInfo.getMonthVal()==0)
            installment_month.setText("일시불");
        else
            installment_month.setText(cardReceiptInfo.getMonthVal()+"");//할부개월
        approval_no.setText(cardReceiptInfo.getApprovalNo());//승인번호
        tid.setText(cardReceiptInfo.getTid());//단말번호

        credit_Card_paper.setVisibility(View.VISIBLE);//카드일땐 전표 보이게 함
    }
    //    public void setListViewHeightBasedOnChildren() {
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager mgr = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
//        mgr.getDefaultDisplay().getMetrics(metrics);
//        int height_px = (int)(Math.ceil((30.0 * (float)metrics.densityDpi /160.0)));//30 = receipt_item 하나당 높이 dp, px 단위로 전환(소수점은 올림해줌)
//        Log.d("TAG", "height_px = " + height_px);
//        int listView_height=0;
//        for(SelectedMenu s :selectedMenus)
//        {
//            int a;
//            a = s.getMenu_name().getBytes().length/30;
//            if(a==0)
//                a++;
//            listView_height= listView_height + height_px + (a-1) * height_px;
//        }
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
//        layoutParams.height = listView_height;//아이템 개수에 따라서 listview의 height 조절해서 충분한 공간 확보
//        listView.setLayoutParams(layoutParams);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish_receipt://영수증발급
                Intent intent = new Intent(this, CouponAndReceiptLoadingPopupActivity.class);
                intent.putExtra("type",22);//21 = 쿠폰로딩 22 = 영수증로딩
                intent.putExtra("phonenumber", phonenumber);
                capture();
                startActivity(intent);
                finish();
                break;
            case R.id.btnCancel_receipt:
                Intent intent2 = new Intent(this, CompleteOrder_PayActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    //
//public static void setListViewHeightBasedOnChildren(ListView listView) {
//    ListAdapter listAdapter = listView.getAdapter();
//    if (listAdapter == null) {
//        // pre-condition
//        return;
//    }
//    int totalHeight = 0;
//    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//    for (int i = 0; i < listAdapter.getCount(); i++) {
//        View listItem = listAdapter.getView(i, null, listView);
//        LinearLayout L = (LinearLayout)listItem;
//        TextView T = (TextView) L.getChildAt(0);
//        TextView T2 = (TextView) L.getChildAt(1);
//
//        Log.d("AAA", "setListViewHeightBasedOnChildren: "+L.getChildCount());
//        Log.d("AAA", "setListViewHeightBasedOnChildren: "+T.getText());
//        Log.d("AAA", "setListViewHeightBasedOnChildren: "+T.getLineCount());
//        Log.d("AAA", "setListViewHeightBasedOnChildren: "+T.getLineHeight());
//
//
//
//
//        //listItem.measure(0, 0);
//        ViewGroup.LayoutParams params = listItem.getLayoutParams();
//        ;
//        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//        Log.d("AAA", "setListViewHeightBasedOnChildren: "+listItem.getMeasuredHeight()+"params.height"+params.height);
//        totalHeight += listItem.getMeasuredHeight();
//    }
//    ViewGroup.LayoutParams params = listView.getLayoutParams();
//    params.height = totalHeight;
//    listView.setLayoutParams(params);
//    listView.requestLayout();
//}

    //화면캡쳐용
    public void capture()
    {
        System.out.println("화면 캡쳐");
        final LinearLayout rootLayout = (LinearLayout)findViewById(R.id.receipt_capture_layout);

        rootLayout.buildDrawingCache();   // 캡처할 뷰를 지정하여 buildDrawingCache() 한다
        Bitmap captureView = rootLayout.getDrawingCache();   // 캡쳐할 뷰를 지정하여 getDrawingCache() 한다

        FileOutputStream fos;   // FileOutputStream 이용 파일 쓰기 한다
        File storage = new File(getFilesDir().getAbsolutePath()+"");
        //File storage = new File("/storage/emulated/0/Android/data/com.example.onlytest/");
        //   System.out.println("save path : "+getCacheDir().getAbsolutePath());

        String od_id = app.getCurrentOrder_id();
        File tempFile = new File(storage,od_id+".png");

        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            captureView.compress(Bitmap.CompressFormat.PNG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }

    }
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

    //타이머 리셋용
    public boolean dispatchTouchEvent (MotionEvent ev){

        return super.dispatchTouchEvent(ev);
    }
}
