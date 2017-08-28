package com.genar.portal.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genar.portal.R;
import com.genar.portal.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDetailFragment extends Fragment {
    @BindView(R.id.userfrag_image)
    CircleImageView imgvImage;
    @BindView(R.id.userfrag_btnclose)
    Button btnClose;
    @BindView(R.id.userfrag_name)
    TextView tvName;
    @BindView(R.id.userfrag_status)
    TextView tvStatus;
    @BindView(R.id.userfrag_phone)
    TextView tvPhone;
    @BindView(R.id.userfrag_email)
    TextView tvEmail;
    @BindView(R.id.userfrag_skype)
    TextView tvSkypeId;
    @BindView(R.id.userfrag_skypelayout)
    LinearLayout llSkype;
    @BindView(R.id.userfrag_phonelayout)
    LinearLayout llPhone;
    @BindView(R.id.userfrag_smslayout)
    LinearLayout llSms;
    @BindView(R.id.userfrag_emaillayout)
    LinearLayout llEmail;
    @BindView(R.id.userFragment_container)
    ConstraintLayout clRootView;

    private Context context;
    private User user;
    FragmentManager fragmentManager;
    String animName;

    public static EmployeeDetailFragment newInstance(User user, String animName) {

        Bundle args = new Bundle();
        args.putParcelable("user",user);
        args.putString("animName", animName);
        EmployeeDetailFragment fragment = new EmployeeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public EmployeeDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
        animName = getArguments().getString("animName");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        ButterKnife.bind(this,view);

        context = getActivity();

        tvName.setText(user.getName());
        tvStatus.setText(user.getStatus());
        tvPhone.setText(user.getPhoneNumber());
        tvEmail.setText(user.getUserName());
        tvSkypeId.setText(user.getSkypeUsername());
        Picasso.with(context)
                .load(user.getProfileImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.login)
                .into(imgvImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.login)
                                .error(R.drawable.cirle)
                                .into(imgvImage);
                    }
                });


        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        anim.setDuration(getResources().getInteger(R.integer.animDurationTransition));
        container.startAnimation(anim);


        // Inflate the layout for this fragment
        return view;
    }

    @OnClick(R.id.userfrag_phonelayout)
    public void callPerson(){
        if(user.getPhoneNumber().length() != 0){
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+user.getPhoneNumber()));
            context.startActivity(callIntent);
        }else{
            Toast.makeText(context, "Seçmiş olduğunuz kişinin telefon numarası kayıtlı değildir.", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.userfrag_smslayout)
    public void smsPerson(){
        if(user.getPhoneNumber().length() != 0){
            Intent intent_sms = new Intent(Intent.ACTION_VIEW);
            intent_sms.setData(Uri.parse("smsto:"+ user.getPhoneNumber()));
            context.startActivity(Intent.createChooser(intent_sms,"... ile mesaj gönder"));
        }else{
            Toast.makeText(context, "Seçmiş olduğunuz kişinin telefon numarası kayıtlı değildir.", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.userfrag_emaillayout)
    public void emailPerson(){
        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",user.getUserName(),null));
        context.startActivity(Intent.createChooser(intent, "E-Mail Gönder"));
    }
    @OnClick(R.id.userfrag_skypelayout)
    public void skypePerson(){
        if(user.getSkypeUsername().length() != 0){
            Uri skypeUri = Uri.parse("skype:" + user.getSkypeUsername() + "?chat");
            Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);
            myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }else{
            Toast.makeText(context, "Seçmiş olduğunuz kişinin SkypeID'si kayıtlı değildir.", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick({R.id.userfrag_btnclose, R.id.userFragment_container})
    public void closeFragment(){
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }


}
