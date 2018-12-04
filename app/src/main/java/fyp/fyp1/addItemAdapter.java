package fyp.fyp1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class addItemAdapter extends RecyclerView.Adapter<MyViewHolder> {
//
    String[] name;
    String[] quantity,contactno,Description;

    ArrayList<Item> itemsArrayList;

    public addItemAdapter() {
    }

    public addItemAdapter(String[] name, String[] quantity, String[] contactno, String[] descrip, ArrayList<Item> itemsArrayList) {
        this.name = name;
        this.quantity = quantity;
        this.contactno = contactno;
        this.Description = descrip;
        this.itemsArrayList = itemsArrayList;
    }
    public addItemAdapter(ArrayList<Item> list) {
        this.itemsArrayList=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.Name.setText(itemsArrayList.get(position).getName());
        holder.quantity.setText(itemsArrayList.get(position).getQuantity());
        holder.contactno.setText(itemsArrayList.get(position).getContactno());
        holder.Description.setText(itemsArrayList.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder {

    TextView Name,quantity,contactno,Description;

    public MyViewHolder(View itemView) {
        super(itemView);

        Name = (TextView) itemView.findViewById(R.id.adapterName);
        quantity = (TextView) itemView.findViewById(R.id.adapterQuantity);
        contactno = (TextView) itemView.findViewById(R.id.adaptercontactno);
        Description = (TextView) itemView.findViewById(R.id.adapterdescrip);

    }
}
