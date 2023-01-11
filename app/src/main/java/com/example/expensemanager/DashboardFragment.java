package com.example.expensemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private boolean isOpen = false;

    private Animation fadeOpen,fadeClose;

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDB;
    private DatabaseReference mExpenseDB;

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    private RecyclerView recyclerIncome;
    private RecyclerView recyclerExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDB = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDB = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        mIncomeDB.keepSynced(true);
        mExpenseDB.keepSynced(true);

        //connect floating buttons
        fab_main_btn = myView.findViewById(R.id.fb_main_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);
        //connect floating text

        fab_income_txt = myView.findViewById(R.id.income_ft_text);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);

        totalIncomeResult = myView.findViewById(R.id.income_set_result);
        totalExpenseResult = myView.findViewById(R.id.expense_set_result);

        recyclerIncome = myView.findViewById(R.id.recycler_income);
        recyclerExpense = myView.findViewById(R.id.recycler_expense);

        //Animation connection
        fadeOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addData();

                if(isOpen){
                    fab_income_btn.startAnimation(fadeClose);
                    fab_expense_btn.startAnimation(fadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(fadeClose);
                    fab_expense_txt.startAnimation(fadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                }else {
                    fab_income_btn.startAnimation(fadeOpen);
                    fab_expense_btn.startAnimation(fadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(fadeOpen);
                    fab_expense_txt.startAnimation(fadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                }
                isOpen=!isOpen;
            }
        });

        mIncomeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalIncome = 0.0F;

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Data data = snapshot1.getValue(Data.class);
                    totalIncome += data.getAmount();
                }
                String sTotalIncome = String.valueOf(totalIncome);
                totalIncomeResult.setText(sTotalIncome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mExpenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalExpense = 0.0F;

                for (DataSnapshot mySnapshot:snapshot.getChildren()){
                    Data data = mySnapshot.getValue(Data.class);

                    totalExpense += data.getAmount();

                }
                String sTotalIncome = String.valueOf(totalExpense);
                totalExpenseResult.setText(sTotalIncome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        recyclerIncome.setHasFixedSize(true);
        recyclerIncome.setLayoutManager(layoutManagerIncome);
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        recyclerExpense.setHasFixedSize(true);
        recyclerExpense.setLayoutManager(layoutManagerExpense);


        return myView;
    }
    //animation

    private void ftAnimation(){
        if(isOpen){
            fab_income_btn.startAnimation(fadeClose);
            fab_expense_btn.startAnimation(fadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(fadeClose);
            fab_expense_txt.startAnimation(fadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

        }else {
            fab_income_btn.startAnimation(fadeOpen);
            fab_expense_btn.startAnimation(fadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(fadeOpen);
            fab_expense_txt.startAnimation(fadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

        }
        isOpen=!isOpen;
    }


    private void addData(){
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDataInsert();
            }
        });
    }
    public void incomeDataInsert(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);
        AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        EditText editAmount = myView.findViewById(R.id.amount_edit);
        final EditText editType = myView.findViewById(R.id.type_edit);
        EditText editNote = myView.findViewById(R.id.note_edit);

        Button saveBtn = myView.findViewById(R.id.save_btn);
        Button cancelBtn = myView.findViewById(R.id.cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)){
                    editType.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(amount)){
                    editAmount.setError("Required");
                    return;
                }
                float finAmount = Float.parseFloat(amount);
                if (TextUtils.isEmpty(note)){
                    editNote.setError("Required");
                    return;
                }

                String id = mIncomeDB.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(finAmount,type,note,id,mDate);

                mIncomeDB.child(id).setValue(data);

                Toast.makeText(getActivity(),"Data inserted!",Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void expenseDataInsert(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);

        AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);

        EditText editAmount = myView.findViewById(R.id.amount_edit);
        final EditText editType = myView.findViewById(R.id.type_edit);
        EditText editNote = myView.findViewById(R.id.note_edit);

        Button saveBtn = myView.findViewById(R.id.save_btn);
        Button cancelBtn = myView.findViewById(R.id.cancel_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exAmount = editAmount.getText().toString().trim();
                String exType = editType.getText().toString().trim();
                String exNote = editNote.getText().toString().trim();


                if (TextUtils.isEmpty(exType)){
                    editType.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(exAmount)){
                    editAmount.setError("Required");
                    return;
                }
                float finAmount = Float.parseFloat(exAmount);
                if (TextUtils.isEmpty(exNote)){
                    editNote.setError("Required");
                    return;
                }

                String id = mExpenseDB.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(finAmount,exType,exNote,id,mDate);
                mExpenseDB.child(id).setValue(data);

                Toast.makeText(getActivity(),"Data inserted!",Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> incomeOptions = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDB,Data.class)
                .build();

        FirebaseRecyclerAdapter incomeAdapter = new FirebaseRecyclerAdapter<Data,IncomeViewHolder>(incomeOptions) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());
                holder.setIncomeDate(model.getDate());
            }

            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new IncomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income,parent,false));
            }
        };
        recyclerIncome.setAdapter(incomeAdapter);
        incomeAdapter.startListening();



        FirebaseRecyclerOptions<Data> expenseOption = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDB,Data.class)
                .build();

        FirebaseRecyclerAdapter expenseAdapter = new FirebaseRecyclerAdapter<Data,ExpenseViewHolder>(expenseOption){

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ExpenseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setExpenseAmount(model.getAmount());
                holder.setExpenseType(model.getType());
                holder.setExpenseDate(model.getDate());
            }
        };
        recyclerExpense.setAdapter(expenseAdapter);
        expenseAdapter.startListening();

    }
    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView=itemView;
        }
        public void setIncomeType(String type){
            TextView mType=mIncomeView.findViewById(R.id.type_income_db);
            mType.setText(type);
        }

        public void setIncomeAmount(float amount){
            TextView mAmount=mIncomeView.findViewById(R.id.amount_income_db);
            String strAmount=String.valueOf(amount);
            Log.i("AMOUNT", strAmount);
            mAmount.setText(strAmount);
        }

        public void setIncomeDate(String date){
            TextView mDate=mIncomeView.findViewById(R.id.date_income_db);
            Log.i("DATE", date);
            mDate.setText(date);
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        View mExpenseView;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView=itemView;
        }
        public void setExpenseType(String type){
            TextView mType=mExpenseView.findViewById(R.id.type_expense_db);
            mType.setText(type);
        }
        public void setExpenseAmount(float amount){
            TextView mAmount=mExpenseView.findViewById(R.id.amount_expense_db);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);

        }
        public void setExpenseDate(String date){
            TextView mDate=mExpenseView.findViewById(R.id.date_expense_db);
            mDate.setText(date);
        }
    }
}