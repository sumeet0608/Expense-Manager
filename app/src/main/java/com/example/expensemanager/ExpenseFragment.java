package com.example.expensemanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class ExpenseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDB;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter1;

    private TextView totalExpenseText;

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    private Button updateBtn;
    private Button deleteBtn;

    //Data variable

    private String type;
    private String note;
    private float amount;
    private String post_key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = mUser.getUid();
        mExpenseDB = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        totalExpenseText = myView.findViewById(R.id.expense_txt_result);
        recyclerView = myView.findViewById(R.id.recycler_expense_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalExpense = 0.0F;

                for (DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data = mySnapshot.getValue(Data.class);

                    totalExpense += data.getAmount();

                }
                String sTotalIncome = String.valueOf(totalExpense);
                totalExpenseText.setText(sTotalIncome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDB,Data.class)
                .build();

        adapter1 = new FirebaseRecyclerAdapter<Data,MyViewHolder>(options) {

            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {
                holder.setAmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();
                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateDataItem();
                    }
                });
            }

            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data,parent,false));
            }
        };
        recyclerView.setAdapter(adapter1);
        adapter1.startListening();

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        void setNote(String note) {

            TextView mNote = mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        void setAmount(float amount) {
            TextView mAmount = mView.findViewById(R.id.amount_txt_expense);
            String stAmount = String.valueOf(amount);
            mAmount.setText(stAmount);
        }
    }

    private void updateDataItem(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_data_item, null);
        myDialog.setView(myView);

        edtAmount = myView.findViewById(R.id.amount_edit);
        edtNote = myView.findViewById(R.id.note_edit);
        edtType = myView.findViewById(R.id.type_edit);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        updateBtn = myView.findViewById(R.id.update_btn);
        deleteBtn = myView.findViewById(R.id.delete_btn);

        final AlertDialog dialog = myDialog.create();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();

                String stAmount = String.valueOf(amount);
                stAmount = edtAmount.getText().toString().trim();
                float finAmount = Float.parseFloat(stAmount);

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(finAmount, type, note, post_key, mDate);

                mExpenseDB.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpenseDB.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}