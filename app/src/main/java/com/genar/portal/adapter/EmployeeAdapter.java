package com.genar.portal.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.genar.portal.R;
import com.genar.portal.fragment.EmployeeDetailFragment;
import com.genar.portal.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.UserViewHolder> {

    private ArrayList<User> userList;
    private Context context;
    private FragmentManager fragmentManager;

    public EmployeeAdapter(ArrayList<User> userList, Context context, FragmentManager fm) {
        this.userList = userList;
        this.context = context;
        this.fragmentManager = fm;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user = userList.get(position);
        animate(holder.cvView,position);

        holder.llLeftLine.setBackgroundResource(R.color.genarDark);

//      Item'ların solundaki çizgilere random renkler verir
        /*Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.llLeftLine.setBackgroundColor(color);*/

        ViewCompat.setTransitionName(holder.imgvProfileImage, user.getUserName());

        if(position == 0){
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) holder.cvView.getLayoutParams();
            layoutParams.topMargin = 32;
            holder.cvView.setLayoutParams(layoutParams);
        }


        holder.tvName.setText(user.getName());
        holder.tvStatus.setText(user.getStatus());
        Picasso.with(context)
                .load(user.getProfileImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
//                .placeholder(R.drawable.login)
                .into(holder.imgvProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Animation fadeOut = new AlphaAnimation(0, 1);
                        fadeOut.setInterpolator(new AccelerateInterpolator());
                        fadeOut.setDuration(1000);
                        holder.imgvProfileImage.startAnimation(fadeOut);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.login)
                                .error(R.drawable.cirle)
                                .into(holder.imgvProfileImage);
                    }
                });

        holder.imgvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getPhoneNumber().length() != 0){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+user.getPhoneNumber()));
                    context.startActivity(callIntent);
                }else{
                    Toast.makeText(context, "Seçmiş olduğunuz kişinin telefon numarası kayıtlı değildir.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getPhoneNumber().length() != 0){
                    Intent intent_sms = new Intent(Intent.ACTION_VIEW);
                    intent_sms.setData(Uri.parse("smsto:"+ user.getPhoneNumber()));
                    context.startActivity(Intent.createChooser(intent_sms,"... ile mesaj gönder"));
                }else{
                    Toast.makeText(context, "Seçmiş olduğunuz kişinin telefon numarası kayıtlı değildir.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto",user.getUserName(),null));
                context.startActivity(Intent.createChooser(intent, "E-Mail Gönder"));

            }
        });
        holder.imgvSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        holder.clClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeDetailFragment udFragment = null;
                udFragment = EmployeeDetailFragment.newInstance(user,null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    udFragment.setEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.fade));
                    udFragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.fade));
                }
                udFragment.setAllowEnterTransitionOverlap(true);

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                        .replace(R.id.userdetailfragment, udFragment, udFragment.getTag())
                        .commit();
            }
        });

    }
    private void animate(View view, final int pos) {
        view.animate().cancel();
//        view.setTranslationX(100);
//        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f)/*.translationX(0).translationY(0)*/.setDuration(500).setStartDelay(pos * 40);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.empitem_leftLine)
        LinearLayout llLeftLine;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ib_call)
        ImageView imgvCall;
        @BindView(R.id.ib_mesaj)
        ImageView imgvMessage;
        @BindView(R.id.ib_email)
        ImageView imgvEmail;
        @BindView(R.id.ib_skype)
        ImageView imgvSkype;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.employee_cardview)
        CardView cvView;
        @BindView(R.id.empitem_image)
        CircleImageView imgvProfileImage;
        @BindView(R.id.employeeclicklayout)
        ConstraintLayout clClick;
        @BindView(R.id.empitem_swipelayout)
        SwipeLayout slSwipe;

        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
