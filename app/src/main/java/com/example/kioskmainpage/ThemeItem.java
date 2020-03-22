package com.example.kioskmainpage;

public class ThemeItem {

    private int BEST_NEW_PASS_RIGHT_IC_ID;//베스트 뉴메뉴의 '전체메뉴' > 아이콘
    private int ADMIN_PASS_LEFT_IC_ID;//admin 메뉴들의 left 화살표 아이콘
    private String theme_name;//테마 이름
    private int TEXT_COLOR_ID;//전체 텍스트 컬러
    private int BACKGROUND_COLOR_ID;//백그라운드 컬러
    private int TAB_COLOR_ID;//메인 메뉴 탭컬러
    private int TAB_SELECTEDTAB_INDICATE_BACKGROUND;//메인메뉴 선택탭 컬러
    private int TAB_TEXT_COLOR_ID;//메인메뉴 탭 텍스트 컬러
    private int TAB_SELECTED_TEXT_COLOR_ID;//메인메뉴 선택 탭 텍스트 컬러
    private int NEW_MENU_BACKGROUND_COLOR_ID;//뉴메뉴 백그라운드 컬러
    private int BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID;//베스트 뉴 백그라운드 컬러
    private int themeCheckImg_IC_ID;
    private int themeNonCheckImg_IC_ID;
    private int themeAddImage_IC_ID;
    private int ADMIN_BACKGROUND_COLOR_ID;
    private int OPTION_POPUP_TEXT_COLOR;//옵션 선택 팝업 텍스트 컬러
    private int OPTION_POPUP_SELECT_TEXT_COLOR;//옵션선택팝업 선택옵션 택스트 컬러
    private int OPTION_TOGGLE_COLOR;//옵션선택 토글 배경
    private int OPTION_TOGGLE_SELECT_COLOR;//옵션선택 선택된 토글 배경
    private int OPTION_POPUP_MENU_NAME_COLOR;//옵션 선택 팝업 메뉴이름 및 가격 텍스트 컬러
    private int OPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR;
    private int TABLE_NUM_TEXT_COLOR;//테이블번호 팝업 텍스트 컬러
    private int TABLE_NUMPAD_TEXT_COLOR;//테이블번호 에디트 텍스트 번호
    private int TABLE_NUMPAD_BACKGROUND_COLOR;//테이블 번호 숫자입력버튼 배경
    private int TABLE_NUMPAD_BACKSPACE_BUTTON;//테이블 번호 입력 지움키 이미지 리소스
    private int TABLE_NUMPAD_ICON_IMAGE;//테이블 번호 입력 화면 아이콘 리소스
    private int CONFIRM_BTN_BACKGROUND_COLOR;//확인키 버튼
    private int CANCEL_BTN_BACKGROUND_COLOR;//취소키 버튼
    private int NUMPAD_BACKGROUND_ROUND_COLOR;//팝업 라운드 배경
    private int MAIN_PAY_BUTTIN_COLOR; //메인 주문하기 버튼
    private int TAKEOUT_TEXT_COLOR;
    private int TAKEOUT_IMAGE;
    private int SHOP_TEXT_COLOR;
    private int SHOP_IMAGE;
    private int TEXT_COLOR;//TAKE OUT 메뉴 선택 텍스트
    private int TAKE_OUT_BACKGROUND_ROUND_COLOR;//TAKE OUT 팝업 라운드 배경
    private int TAKE_OUT_BACKGROUND_ROUND_COLOR2;//TAKE OUT 두번째 팝업 라운드 배경
    private int TAKEOUT_SECOND_TEXT_COLOR;//TAKE OUT 두번째 팝업 글자 색상
    private int MAIN_SELECTEDLIST_TEXT_COLOR;//메뉴선택 바텀 레이아웃 텍스트 컬러
    private int SELECTED_RESET_TEXT_COLOR; //메인 선택 리셋 버튼
    private int SELECTED_RESET_TEXT_HIGHLIGHT; //메인 선택 리셋 하이라이트
    private int SELECTED_RESET_TEXT_SECONDS;

    public ThemeItem(String theme_name)
    {
        this.theme_name = theme_name;
        if (theme_name.equals("WHITE"))
        {
            setTEXT_COLOR_ID(R.color.black);
            setBACKGROUND_COLOR_ID(R.color.white);
            setTAB_COLOR_ID(R.color.dark_theme_color);
            setTAB_SELECTEDTAB_INDICATE_BACKGROUND(R.color.dark_theme_color);
            setTAB_TEXT_COLOR_ID(R.color.light_gray);
            setTAB_SELECTED_TEXT_COLOR_ID(R.color.white);
            setNEW_MENU_BACKGROUND_COLOR_ID(R.color.white_theme_color);
            setBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID(R.color.white);
            setBEST_NEW_PASS_RIGHT_IC_ID(R.drawable.ic_pass_right);
            setADMIN_PASS_LEFT_IC_ID(R.drawable.ic_pass_left);
            setThemeCheckImg_IC_ID(R.drawable.white_skin_check_img);
            setThemeNonCheckImg_IC_ID(R.drawable.white_skin_img);
            setThemeAddImage_IC_ID(R.drawable.theme_add_black_img);
            setADMIN_BACKGROUND_COLOR_ID(R.color.admin_background_white);
            setOPTION_POPUP_TEXT_COLOR(R.color.black);
            setOPTION_POPUP_SELECT_TEXT_COLOR(R.color.white);
            setOPTION_TOGGLE_COLOR(R.color.light_gray_2);
            setOPTION_TOGGLE_SELECT_COLOR(R.color.gray_2);
            setOPTION_POPUP_MENU_NAME_COLOR(R.color.black);
            setOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR(R.color.white);
            setMAIN_SELECTEDLIST_TEXT_COLOR(R.color.black);
            setTABLE_NUM_TEXT_COLOR(R.color.black);
            setTABLE_NUMPAD_TEXT_COLOR(R.color.black);
            setTABLE_NUMPAD_BACKGROUND_COLOR(R.drawable.ripple_numberpad);
            setTABLE_NUMPAD_BACKSPACE_BUTTON(R.drawable.ic_backspace_black_24dp);
            setTABLE_NUMPAD_ICON_IMAGE(R.drawable.ic_ico_foodtray);
            setCANCEL_BTN_BACKGROUND_COLOR(R.drawable.round_button_left_c1c1c1);
            setCONFIRM_BTN_BACKGROUND_COLOR(R.drawable.round_button_right_000000);
            setNUMPAD_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup);
            setMAIN_PAY_BUTTIN_COLOR(R.drawable.ripple_main_confirm);
            setTAKEOUT_TEXT_COLOR(R.color.black);
            setTAKEOUT_IMAGE(R.drawable.ic_ico_takeout);
            setSHOP_TEXT_COLOR(R.color.black);
            setSHOP_IMAGE(R.drawable.ic_ico_shop);
            setTEXT_COLOR(R.color.black);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR2(R.drawable.round_background_popup);
            setTAKEOUT_SECOND_TEXT_COLOR(R.color.black);
            setSELECTED_RESET_TEXT_COLOR(R.color.black);
            setSELECTED_RESET_TEXT_HIGHLIGHT(R.color.rounded_red);
            setSELECTED_RESET_TEXT_SECONDS(R.color.red);
        }
        else if(theme_name.equals("BLACK"))
        {
            setTEXT_COLOR_ID(R.color.white);
            setBACKGROUND_COLOR_ID(R.color.black);
            setTAB_COLOR_ID(R.color.dark_theme_color);
            setTAB_SELECTEDTAB_INDICATE_BACKGROUND(R.color.dark_theme_color);
            setTAB_TEXT_COLOR_ID(R.color.light_gray);
            setTAB_SELECTED_TEXT_COLOR_ID(R.color.white);
            setNEW_MENU_BACKGROUND_COLOR_ID(R.color.dark_theme_color);
            setBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID(R.color.black);
            setBEST_NEW_PASS_RIGHT_IC_ID(R.drawable.ic_pass_right_white);
            setADMIN_PASS_LEFT_IC_ID(R.drawable.ic_pass_left_white);
            setThemeCheckImg_IC_ID(R.drawable.black_skin_check_img);
            setThemeNonCheckImg_IC_ID(R.drawable.black_skin_img);
            setThemeAddImage_IC_ID(R.drawable.theme_add_white_img);
            setADMIN_BACKGROUND_COLOR_ID(R.color.admin_background_black);
            setOPTION_POPUP_TEXT_COLOR(R.color.white);
            setOPTION_POPUP_SELECT_TEXT_COLOR(R.color.white);
            setOPTION_TOGGLE_COLOR(R.color.light_gray_2);
            setOPTION_TOGGLE_SELECT_COLOR(R.color.gray_2);
            setOPTION_POPUP_MENU_NAME_COLOR(R.color.white);
            setOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR(R.color.white);
            setMAIN_SELECTEDLIST_TEXT_COLOR(R.color.white);
            setTABLE_NUM_TEXT_COLOR(R.color.white);
            setTABLE_NUMPAD_TEXT_COLOR(R.color.black);
            setTABLE_NUMPAD_BACKGROUND_COLOR(R.drawable.ripple_numberpad);
            setTABLE_NUMPAD_BACKSPACE_BUTTON(R.drawable.ic_backspace_black_24dp);
            setTABLE_NUMPAD_ICON_IMAGE(R.drawable.ic_ico_foodtray_theme3);
            setCANCEL_BTN_BACKGROUND_COLOR(R.drawable.round_button_left_c1c1c1);
            setCONFIRM_BTN_BACKGROUND_COLOR(R.drawable.round_button_right_000000);
            setNUMPAD_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup_theme3);
            setMAIN_PAY_BUTTIN_COLOR(R.drawable.ripple_main_confirm);
            setTAKEOUT_TEXT_COLOR(R.color.white);
            setTAKEOUT_IMAGE(R.drawable.ic_ico_takeout1);
            setSHOP_TEXT_COLOR(R.color.white);
            setSHOP_IMAGE(R.drawable.ic_ico_shop1);
            setTEXT_COLOR(R.color.white);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup_theme3);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR2(R.drawable.round_background_popup_theme3);
            setTAKEOUT_SECOND_TEXT_COLOR(R.color.white);
            setSELECTED_RESET_TEXT_COLOR(R.color.white);
            setSELECTED_RESET_TEXT_HIGHLIGHT(R.color.rounded_red);
            setSELECTED_RESET_TEXT_SECONDS(R.color.red);
        }
        else if(theme_name.equals("SCHOOL_FOOD"))
            {
            setTEXT_COLOR_ID(R.color.e20000_red);
            setBACKGROUND_COLOR_ID(R.color.dark_gray_theme);
            setTAB_COLOR_ID(R.color.dark_gray_theme);
            setTAB_SELECTEDTAB_INDICATE_BACKGROUND(R.color.e20000_red);
            setTAB_TEXT_COLOR_ID(R.color.white);
            setTAB_SELECTED_TEXT_COLOR_ID(R.color.e20000_red);
            setNEW_MENU_BACKGROUND_COLOR_ID(R.color.dark_gray_theme);
            setBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID(R.color.dark_gray_theme);
            setBEST_NEW_PASS_RIGHT_IC_ID(R.drawable.ic_pass_right_white);
            setADMIN_PASS_LEFT_IC_ID(R.drawable.ic_pass_left_white);
            setThemeCheckImg_IC_ID(R.drawable.black_skin_check_img);
            setThemeNonCheckImg_IC_ID(R.drawable.black_skin_img);
            setThemeAddImage_IC_ID(R.drawable.theme_add_white_img);
            setADMIN_BACKGROUND_COLOR_ID(R.color.dark_gray_theme);
            setOPTION_POPUP_TEXT_COLOR(R.color.e20000_red);
            setOPTION_POPUP_SELECT_TEXT_COLOR(R.color.white);
            setOPTION_TOGGLE_COLOR(R.color.white);
            setOPTION_TOGGLE_SELECT_COLOR(R.color.dark_red);
            setOPTION_POPUP_MENU_NAME_COLOR(R.color.white);
            setOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR(R.color.white);
            setMAIN_SELECTEDLIST_TEXT_COLOR(R.color.white);
            setTABLE_NUM_TEXT_COLOR(R.color.white);
            setTABLE_NUMPAD_TEXT_COLOR(R.color.white);
            setTABLE_NUMPAD_BACKGROUND_COLOR(R.color.dark_gray_theme);
            setTABLE_NUMPAD_BACKSPACE_BUTTON(R.drawable.ic_backspace_black_24dp_theme3);
            setTABLE_NUMPAD_ICON_IMAGE(R.drawable.ic_ico_foodtray_theme3);
            setCANCEL_BTN_BACKGROUND_COLOR(R.drawable.round_button_left_c1c1c1);
            setCONFIRM_BTN_BACKGROUND_COLOR(R.drawable.round_button_right_8a0808);
            setNUMPAD_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup_theme3);
            setMAIN_PAY_BUTTIN_COLOR(R.drawable.ripple_main_confirm);
            setTAKEOUT_TEXT_COLOR(R.color.white);
            setTAKEOUT_IMAGE(R.drawable.ic_ico_takeout2);
            setSHOP_TEXT_COLOR(R.color.white);
            setSHOP_IMAGE(R.drawable.ic_ico_shop2);
            setTEXT_COLOR(R.color.white);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR(R.drawable.round_background_popup_theme3);
            setTAKE_OUT_BACKGROUND_ROUND_COLOR2(R.drawable.round_background_popup_theme3);
            setTAKEOUT_SECOND_TEXT_COLOR(R.color.white);
            setSELECTED_RESET_TEXT_COLOR(R.color.white);
            setSELECTED_RESET_TEXT_HIGHLIGHT(R.color.rounded_red);
            setSELECTED_RESET_TEXT_SECONDS(R.color.red);
        }
    }
    public ThemeItem(String theme_name, int BEST_NEW_PASS_RIGHT_IC_ID, int TEXT_COLOR_ID, int BACKGROUND_COLOR_ID, int TAB_COLOR_ID,
                     int NEW_MENU_BACKGROUND_COLOR_ID, int BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID, int themeCheckImg_IC_ID, int themeNonCheckImg_IC_ID,
                     int themeAddImage_IC_ID)
    {
        this.theme_name=theme_name;
        this.BEST_NEW_PASS_RIGHT_IC_ID=BEST_NEW_PASS_RIGHT_IC_ID;
        this.TEXT_COLOR_ID = TEXT_COLOR_ID;
        this.TAB_COLOR_ID = TAB_COLOR_ID;
        this.NEW_MENU_BACKGROUND_COLOR_ID = NEW_MENU_BACKGROUND_COLOR_ID;
        this.BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID = BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID;
        this.themeCheckImg_IC_ID = themeCheckImg_IC_ID;
        this.themeNonCheckImg_IC_ID = themeNonCheckImg_IC_ID;
        this.themeAddImage_IC_ID = themeAddImage_IC_ID;

    }
    public int getTAKEOUT_SECOND_TEXT_COLOR(){return TAKEOUT_SECOND_TEXT_COLOR;}

    public void setTAKEOUT_SECOND_TEXT_COLOR(int TAKEOUT_SECOND_TEXT_COLOR){
        this.TAKEOUT_SECOND_TEXT_COLOR = TAKEOUT_SECOND_TEXT_COLOR;
    }

    public int getSELECTED_RESET_TEXT_SECONDS(){return SELECTED_RESET_TEXT_SECONDS;}

    public void setSELECTED_RESET_TEXT_SECONDS(int SELECTED_RESET_TEXT_SECONDS){
        this.SELECTED_RESET_TEXT_SECONDS = SELECTED_RESET_TEXT_SECONDS;
    }

    public int getSELECTED_RESET_TEXT_HIGHLIGHT() {return SELECTED_RESET_TEXT_HIGHLIGHT;}

    public void setSELECTED_RESET_TEXT_HIGHLIGHT(int SELECTED_RESET_TEXT_HIGHLIGHT){
        this.SELECTED_RESET_TEXT_HIGHLIGHT = SELECTED_RESET_TEXT_HIGHLIGHT;
    }

    public int getTAKE_OUT_BACKGROUND_ROUND_COLOR2(){return TAKE_OUT_BACKGROUND_ROUND_COLOR2;}

    public void setTAKE_OUT_BACKGROUND_ROUND_COLOR2(int TAKE_OUT_BACKGROUND_ROUND_COLOR2){
        this.TAKE_OUT_BACKGROUND_ROUND_COLOR2 = TAKE_OUT_BACKGROUND_ROUND_COLOR2;
    }

    public int getTAKE_OUT_BACKGROUND_ROUND_COLOR(){return TAKE_OUT_BACKGROUND_ROUND_COLOR;}

    public void setTAKE_OUT_BACKGROUND_ROUND_COLOR(int TAKE_OUT_BACKGROUND_ROUND_COLOR){
        this.TAKE_OUT_BACKGROUND_ROUND_COLOR = TAKE_OUT_BACKGROUND_ROUND_COLOR;
    }
    public int getTEXT_COLOR(){return TEXT_COLOR;}

    public void setTEXT_COLOR(int TEXT_COLOR){
        this.TEXT_COLOR = TEXT_COLOR;
    }
    public int getSHOP_IMAGE(){return SHOP_IMAGE;}

    public void setSHOP_IMAGE(int SHOP_IMAGE){
        this.SHOP_IMAGE = SHOP_IMAGE;
    }
    public int getSHOP_TEXT_COLOR(){return SHOP_TEXT_COLOR;}

    public void setSHOP_TEXT_COLOR(int SHOP_TEXT_COLOR){
        this.SHOP_TEXT_COLOR = SHOP_TEXT_COLOR;
    }
    public int getTAKEOUT_IMAGE(){return TAKEOUT_IMAGE;}

    public void setTAKEOUT_IMAGE(int TAKEOUT_IMAGE){
        this.TAKEOUT_IMAGE = TAKEOUT_IMAGE;
    }

    public int getTAKEOUT_TEXT_COLOR(){return TAKEOUT_TEXT_COLOR;}

    public void setTAKEOUT_TEXT_COLOR(int TAKEOUT_TEXT_COLOR){
        this.TAKEOUT_TEXT_COLOR = TAKEOUT_TEXT_COLOR;
    }

    public int getOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR() {
        return OPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR;
    }

    public void setOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR(int OPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR) {
        this.OPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR = OPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR;
    }

    public int getMAIN_SELECTEDLIST_TEXT_COLOR() {
        return MAIN_SELECTEDLIST_TEXT_COLOR;
    }

    public void setMAIN_SELECTEDLIST_TEXT_COLOR(int MAIN_SELECTEDLIST_TEXT_COLOR) {
        this.MAIN_SELECTEDLIST_TEXT_COLOR = MAIN_SELECTEDLIST_TEXT_COLOR;
    }

    public int getNUMPAD_BACKGROUND_ROUND_COLOR() {
        return NUMPAD_BACKGROUND_ROUND_COLOR;
    }

    public void setNUMPAD_BACKGROUND_ROUND_COLOR(int NUMPAD_BACKGROUND_ROUND_COLOR) {
        this.NUMPAD_BACKGROUND_ROUND_COLOR = NUMPAD_BACKGROUND_ROUND_COLOR;
    }

    public int getSELECTED_RESET_TEXT_COLOR() { return SELECTED_RESET_TEXT_COLOR; }

    public void setSELECTED_RESET_TEXT_COLOR(int SELECTED_RESET_TEXT_COLOR) {
        this.SELECTED_RESET_TEXT_COLOR = SELECTED_RESET_TEXT_COLOR;
    }

    public int getCANCEL_BTN_BACKGROUND_COLOR() {
        return CANCEL_BTN_BACKGROUND_COLOR;
    }

    public void setCANCEL_BTN_BACKGROUND_COLOR(int CANCEL_BTN_BACKGROUND_COLOR) {
        this.CANCEL_BTN_BACKGROUND_COLOR = CANCEL_BTN_BACKGROUND_COLOR;
    }

    public int getMAIN_PAY_BUTTIN_COLOR() { return MAIN_PAY_BUTTIN_COLOR;}

    public void setMAIN_PAY_BUTTIN_COLOR(int MAIN_PAY_BUTTIN_COLOR) {
        this.MAIN_PAY_BUTTIN_COLOR = MAIN_PAY_BUTTIN_COLOR;
    }


    public int getCONFIRM_BTN_BACKGROUND_COLOR() {
        return CONFIRM_BTN_BACKGROUND_COLOR;
    }

    public void setCONFIRM_BTN_BACKGROUND_COLOR(int CONFIRM_BTN_BACKGROUND_COLOR) {
        this.CONFIRM_BTN_BACKGROUND_COLOR = CONFIRM_BTN_BACKGROUND_COLOR;
    }

    public int getTABLE_NUMPAD_ICON_IMAGE() {
        return TABLE_NUMPAD_ICON_IMAGE;
    }

    public void setTABLE_NUMPAD_ICON_IMAGE(int TABLE_NUMPAD_ICON_IMAGE) {
        this.TABLE_NUMPAD_ICON_IMAGE = TABLE_NUMPAD_ICON_IMAGE;
    }

    public int getTABLE_NUMPAD_BACKSPACE_BUTTON() {
        return TABLE_NUMPAD_BACKSPACE_BUTTON;
    }

    public void setTABLE_NUMPAD_BACKSPACE_BUTTON(int TABLE_NUMPAD_BACKSPACE_BUTTON) {
        this.TABLE_NUMPAD_BACKSPACE_BUTTON = TABLE_NUMPAD_BACKSPACE_BUTTON;
    }

    public int getTABLE_NUMPAD_BACKGROUND_COLOR() {
        return TABLE_NUMPAD_BACKGROUND_COLOR;
    }

    public void setTABLE_NUMPAD_BACKGROUND_COLOR(int TABLE_NUMPAD_BACKGROUND_COLOR) {
        this.TABLE_NUMPAD_BACKGROUND_COLOR = TABLE_NUMPAD_BACKGROUND_COLOR;
    }

    public int getTABLE_NUMPAD_TEXT_COLOR() {
        return TABLE_NUMPAD_TEXT_COLOR;
    }

    public void setTABLE_NUMPAD_TEXT_COLOR(int TABLE_NUMPAD_TEXT_COLOR) {
        this.TABLE_NUMPAD_TEXT_COLOR = TABLE_NUMPAD_TEXT_COLOR;
    }

    public int getTABLE_NUM_TEXT_COLOR() {
        return TABLE_NUM_TEXT_COLOR;
    }

    public void setTABLE_NUM_TEXT_COLOR(int TABLE_NUM_TEXT_COLOR) {
        this.TABLE_NUM_TEXT_COLOR = TABLE_NUM_TEXT_COLOR;
    }

    public int getOPTION_POPUP_MENU_NAME_COLOR() {
        return OPTION_POPUP_MENU_NAME_COLOR;
    }

    public void setOPTION_POPUP_MENU_NAME_COLOR(int OPTION_POPUP_MENU_NAME_COLOR) {
        this.OPTION_POPUP_MENU_NAME_COLOR = OPTION_POPUP_MENU_NAME_COLOR;
    }

    public int getOPTION_TOGGLE_SELECT_COLOR() {
        return OPTION_TOGGLE_SELECT_COLOR;
    }

    public void setOPTION_TOGGLE_SELECT_COLOR(int OPTION_TOGGLE_SELECT_COLOR) {
        this.OPTION_TOGGLE_SELECT_COLOR = OPTION_TOGGLE_SELECT_COLOR;
    }

    public int getOPTION_TOGGLE_COLOR() {
        return OPTION_TOGGLE_COLOR;
    }

    public void setOPTION_TOGGLE_COLOR(int OPTION_TOGGLE_COLOR) {
        this.OPTION_TOGGLE_COLOR = OPTION_TOGGLE_COLOR;
    }

    public int getOPTION_POPUP_SELECT_TEXT_COLOR() {
        return OPTION_POPUP_SELECT_TEXT_COLOR;
    }

    public void setOPTION_POPUP_SELECT_TEXT_COLOR(int OPTION_POPUP_SELECT_TEXT_COLOR) {
        this.OPTION_POPUP_SELECT_TEXT_COLOR = OPTION_POPUP_SELECT_TEXT_COLOR;
    }

    public int getOPTION_POPUP_TEXT_COLOR() {
        return OPTION_POPUP_TEXT_COLOR;
    }

    public void setOPTION_POPUP_TEXT_COLOR(int OPTION_POPUP_TEXT_COLOR) {
        this.OPTION_POPUP_TEXT_COLOR = OPTION_POPUP_TEXT_COLOR;
    }

    public int getADMIN_BACKGROUND_COLOR_ID() {
        return ADMIN_BACKGROUND_COLOR_ID;
    }

    public void setADMIN_BACKGROUND_COLOR_ID(int ADMIN_BACKGROUND_COLOR_ID) {
        this.ADMIN_BACKGROUND_COLOR_ID = ADMIN_BACKGROUND_COLOR_ID;
    }

    public int getBEST_NEW_PASS_RIGHT_IC_ID() {
        return BEST_NEW_PASS_RIGHT_IC_ID;
    }

    public void setBEST_NEW_PASS_RIGHT_IC_ID(int BEST_NEW_PASS_RIGHT_IC_ID) {
        this.BEST_NEW_PASS_RIGHT_IC_ID = BEST_NEW_PASS_RIGHT_IC_ID;
    }

    public int getADMIN_PASS_LEFT_IC_ID() {
        return ADMIN_PASS_LEFT_IC_ID;
    }

    public void setADMIN_PASS_LEFT_IC_ID(int ADMIN_PASS_LEFT_IC_ID) {
        this.ADMIN_PASS_LEFT_IC_ID = ADMIN_PASS_LEFT_IC_ID;
    }

    public int getThemeCheckImg_IC_ID() {
        return themeCheckImg_IC_ID;
    }

    public void setThemeCheckImg_IC_ID(int themeCheckImg_IC_ID) {
        this.themeCheckImg_IC_ID = themeCheckImg_IC_ID;
    }

    public int getThemeNonCheckImg_IC_ID() {
        return themeNonCheckImg_IC_ID;
    }

    public void setThemeNonCheckImg_IC_ID(int themeNonCheckImg_IC_ID) {
        this.themeNonCheckImg_IC_ID = themeNonCheckImg_IC_ID;
    }

    public int getThemeAddImage_IC_ID() {
        return themeAddImage_IC_ID;
    }

    public void setThemeAddImage_IC_ID(int themeAddImage_IC_ID) {
        this.themeAddImage_IC_ID = themeAddImage_IC_ID;
    }

    public int getTAB_SELECTEDTAB_INDICATE_BACKGROUND() {
        return TAB_SELECTEDTAB_INDICATE_BACKGROUND;
    }

    public void setTAB_SELECTEDTAB_INDICATE_BACKGROUND(int TAB_SELECTEDTAB_INDICATE_BACKGROUND) {
        this.TAB_SELECTEDTAB_INDICATE_BACKGROUND = TAB_SELECTEDTAB_INDICATE_BACKGROUND;
    }

    public int getTAB_TEXT_COLOR_ID() {
        return TAB_TEXT_COLOR_ID;
    }

    public void setTAB_TEXT_COLOR_ID(int TAB_TEXT_COLOR_ID) {
        this.TAB_TEXT_COLOR_ID = TAB_TEXT_COLOR_ID;
    }

    public int getTAB_SELECTED_TEXT_COLOR_ID() {
        return TAB_SELECTED_TEXT_COLOR_ID;
    }

    public void setTAB_SELECTED_TEXT_COLOR_ID(int TAB_SELECTED_TEXT_COLOR_ID) {
        this.TAB_SELECTED_TEXT_COLOR_ID = TAB_SELECTED_TEXT_COLOR_ID;
    }


    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public int getTEXT_COLOR_ID() {
        return TEXT_COLOR_ID;
    }

    public void setTEXT_COLOR_ID(int TEXT_COLOR_ID) {
        this.TEXT_COLOR_ID = TEXT_COLOR_ID;
    }

    public int getBACKGROUND_COLOR_ID() {
        return BACKGROUND_COLOR_ID;
    }

    public void setBACKGROUND_COLOR_ID(int BACKGROUND_COLOR_ID) {
        this.BACKGROUND_COLOR_ID = BACKGROUND_COLOR_ID;
    }

    public int getTAB_COLOR_ID() {
        return TAB_COLOR_ID;
    }

    public void setTAB_COLOR_ID(int TAB_COLOR_ID) {
        this.TAB_COLOR_ID = TAB_COLOR_ID;
    }

    public int getNEW_MENU_BACKGROUND_COLOR_ID() {
        return NEW_MENU_BACKGROUND_COLOR_ID;
    }

    public void setNEW_MENU_BACKGROUND_COLOR_ID(int NEW_MENU_BACKGROUND_COLOR_ID) {
        this.NEW_MENU_BACKGROUND_COLOR_ID = NEW_MENU_BACKGROUND_COLOR_ID;
    }

    public int getBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID() {
        return BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID;
    }

    public void setBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID(int BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID) {
        this.BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID = BEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID;
    }
}
