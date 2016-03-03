package com.boredream.bdcodehelper.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.boredream.bdcodehelper.R;

public class StringUtils {

    /**
     * String是否为空字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim())
                || "null".equals(str.trim());
    }

//    /**
//     * 生成评论字符串,格式为
//     * bore：blabla 或者 bore回复dream：blablabla
//     */
//    public static SpannableString genCommentString(final Context context, TextView tv, Comment comment) {
//        // 先拼成所需字符串
//        StringBuilder sb = new StringBuilder();
//        // 评论发送者
//        long userId = comment.getUser_id();
//        String userName = comment.getSend_nickname();
//        sb.append(userName);
//        //　评论回复者
//        long toUserId = comment.getTo_user_id();
//        String toUserName = comment.getTo_user_nickname();
//        // 回复用户名的起始位置
//        int replyStart = -1;
//        if (toUserName != null) {
//            // 回复
//            String reply = "回复";
//            sb.append(reply);
//            replyStart = sb.length();
//            sb.append(toUserName);
//        }
//        sb.append("：" + comment.getContent());
//
//        final SpannableString spannableString = EmotionUtils.getEmotionContent(context, tv, sb.toString());
//
//        // 发送用户名字变色
//        UserInfo.UserEntity user = new UserInfo.UserEntity();
//        user.setUser_id(userId);
//        user.setName(userName);
//        spannableString.setSpan(new UserClickableSpan(context, user),
//                0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        if (toUserName != null) {
//            // 回复用户名字变色
//            UserInfo.UserEntity toUser = new UserInfo.UserEntity();
//            toUser.setUser_id(toUserId);
//            toUser.setName(toUserName);
//            spannableString.setSpan(new UserClickableSpan(context, toUser),
//                    replyStart, replyStart + toUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        return spannableString;
//    }

//    public static SpannableStringBuilder getLikesString(final Context context, List<LikeEntity> likeList) {
//        SpannableStringBuilder spannableString = new SpannableStringBuilder();
//
//        // 起始的图标
//        String imageStr = "icon";
//        spannableString.append(imageStr);
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_liked);
//        if (bitmap != null) {
//            int size = DisplayUtils.dp2px(context, 14);
//            size *= 1.2;
//            bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
//
//            ImageSpan imageSpan = new ImageSpan(context, bitmap);
//            spannableString.setSpan(imageSpan, 0, imageStr.length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        spannableString.append(" ");
//
//        // 点赞者名字列表
//        for (int i = 0; i < likeList.size(); i++) {
//            final LikeEntity like = likeList.get(i);
//            if (i > 0) {
//                spannableString.append("，");
//            }
//            int start = spannableString.length();
//            // 点赞的名字拼起来,逗号隔开
//            spannableString.append(like.getNickname());
//
//            UserInfo.UserEntity user = new UserInfo.UserEntity();
//            user.setUser_id(like.getId());
//            user.setName(like.getNickname());
//            spannableString.setSpan(new UserClickableSpan(context, user),
//                    start, start + like.getNickname().length(),
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        return spannableString;
//    }

    public static class PrimaryClickableSpan extends ClickableSpan {

        private Context context;

        public PrimaryClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
            // do nothing
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.colorPrimary));
            ds.setUnderlineText(false);
        }
    }
}
