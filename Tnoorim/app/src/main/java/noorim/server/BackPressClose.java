package noorim.server;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by YuJin on 2015-05-28.
 *
 * 두 번 뒤로가기 버튼에 대한 설정 클래스
 */
public class BackPressClose {

    // 뒤로가기 버튼을 누른 시간
    private long backKeyPressedTime = 0;

    // 액티비티를 입력 받을 변수
    private Activity activity;

    private Toast toast;

    public BackPressClose(Activity activity) {
        this.activity = activity;
    }

    public void BackClose() {
        // 한 번의 뒤로가기 버튼이 눌린 후 현재 시간을 변수에 저장
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        // 한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번 더 눌리면 현재 액티비티 호출
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    // 한 번 뒤로가기 누를 경우 출력
    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}
