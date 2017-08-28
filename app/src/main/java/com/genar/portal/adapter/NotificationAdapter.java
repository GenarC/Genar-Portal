package com.genar.portal.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.klinker.android.peekview.PeekViewActivity;
import com.klinker.android.peekview.builder.Peek;
import com.klinker.android.peekview.builder.PeekViewOptions;
import com.klinker.android.peekview.callback.OnPeek;
import com.genar.portal.R;
import com.genar.portal.model.Notification;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private ArrayList<Notification> notfList;
    private Context context;
    private String role;

    public NotificationAdapter(String role,ArrayList<Notification> notfList, Context context) {
        this.notfList = notfList;
        this.context = context;
        this.role = role;
    }

    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.NotificationViewHolder holder, final int position) {
        final Notification notf = notfList.get(notfList.size()-position-1);
        holder.cvView.setAlpha(0f);
        animate(holder.cvView,position);

        PeekViewOptions options = new PeekViewOptions();
        options.setHapticFeedback(true);

        Peek.into(R.layout.peek_notificationbody, new OnPeek() {
            @Override
            public void onInflated(View rootView) {
                ((TextView) rootView.findViewById(R.id.peek_text))
                        .setText(notfList.get(notfList.size()-position-1).getPost());
            }

            @Override
            public void shown() {

            }

            @Override
            public void dismissed() {

            }
        }).with(options).applyTo((PeekViewActivity) context, holder.peekView);

        if(role.equals("admin")){
            holder.slSwipe.addDrag(SwipeLayout.DragEdge.Right, holder.vRightView);
            holder.slSwipe.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    FirebaseDatabase.getInstance().getReference().child("Duyurular").child(notf.getPostId()).removeValue();
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
        }else{
            holder.slSwipe.setRightSwipeEnabled(false);
        }



        if(position == 0){
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) holder.cvView.getLayoutParams();
            layoutParams.topMargin = 32;
            holder.cvView.setLayoutParams(layoutParams);
        }

        Picasso.with(context)
                .load(notf.getUserImageLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.login)
                .into(holder.imgvProfileImg, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(notf.getUserImageLink())
                                .placeholder(R.drawable.login)
                                .into(holder.imgvProfileImg);
                    }
                });
        holder.tvPost.setText(notf.getPost());
        holder.tvTitle.setText(notf.getTitle());
        holder.tvUserName.setText(notf.getName());
        holder.tvDate.setText(notf.getDate());

    }

    private void animate(View view, final int pos) {
        view.animate().cancel();
        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(500).setStartDelay(pos * 100);
    }

    @Override
    public int getItemCount() {
        return notfList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.notfitem_profileimg)
        ImageView imgvProfileImg;
        @BindView(R.id.notfitem_username)
        TextView tvUserName;
        @BindView(R.id.notfitem_title)
        TextView tvTitle;
        @BindView(R.id.notfitem_post)
        TextView tvPost;
        @BindView(R.id.notfitem_date)
        TextView tvDate;
        @BindView(R.id.notification_cardView)
        CardView cvView;
        @BindView(R.id.notfitem_btndelete)
        ImageButton ibtnDelete;
        @BindView(R.id.notfitem_swipelayout)
        SwipeLayout slSwipe;
        @BindView(R.id.notfitem_rightview)
        View vRightView;
        @BindView(R.id.notfitem_constlayout)
        ConstraintLayout peekView;

        NotificationViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            cvView.setAlpha(0f);
        }
    }
}
