package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by lawson on 2/21/16.
 */
public class ComposeDialog extends DialogFragment {

    public static interface OnCompleteListener {
        public abstract void onDialogComplete(String tweetBody);
    }

    private OnCompleteListener mListener;

    public ComposeDialog() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.compose_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = (User) getArguments().getParcelable("user");

        //----- Setting up the view

        TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        final EditText etBody = (EditText) view.findViewById(R.id.etBody);
        ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        Button btnTweet = (Button) view.findViewById(R.id.btnTweet);

        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);

        //----- Button Click Listeners

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogComplete(etBody.getText().toString());
                dismiss();
            }
        });
    }
}
