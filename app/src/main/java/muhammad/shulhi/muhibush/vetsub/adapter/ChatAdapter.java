package muhammad.shulhi.muhibush.vetsub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import muhammad.shulhi.muhibush.vetsub.R;
import muhammad.shulhi.muhibush.vetsub.model.Message;

/**
 * Created by afdol on 8/4/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<Message> messages;

    public ChatAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        if (!messages.get(position).isSelf()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View viewLeft = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_left, parent, false);
                MyViewHolder viewHolderLeft = new MyViewHolder(viewLeft);
                return viewHolderLeft;
            case 1:
                View viewRight = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_right, parent, false);
                MyViewHolder viewHolderRight = new MyViewHolder(viewRight);
                return viewHolderRight;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.tvMsg.setText(message.getMessage());
        holder.tvMsgFrom.setText(message.getFromName());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsgFrom, tvMsg;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvMsgFrom = (TextView) itemView.findViewById(R.id.tv_msg_from);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
        }
    }
}
