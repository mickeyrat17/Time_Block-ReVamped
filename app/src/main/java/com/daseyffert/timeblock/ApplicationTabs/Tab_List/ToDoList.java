package com.daseyffert.timeblock.ApplicationTabs.Tab_List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daseyffert.timeblock.ApplicationTabs.Tab_List.Database.NoteDbSchema;
import com.daseyffert.timeblock.ApplicationTabs.Tab_List.SingleNote.SingleNoteActivity;
import com.daseyffert.timeblock.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 12/21/2015.
 * Note Taking Fragment
 */
public class ToDoList extends Fragment {

    private TextView mToDoTitleTextView;
    private TextView mPromptTextView;
    private ImageButton mAddNoteButton;
    private RecyclerView mNotesRecyclerView;
    private NoteAdapter mNoteAdapter;

    SQLiteDatabase db;
    NoteDbSchema dbSchema;

    @Override
    public void onResume() {
        super.onResume();
        UpdateUI();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //initialize db and dbSchema
        dbSchema = new NoteDbSchema(getActivity());
        db = dbSchema.getReadableDatabase();

        //Inflate the View
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        //Wire up views
        mToDoTitleTextView = (TextView) view.findViewById(R.id.fragment_notes_list_title);
        mPromptTextView = (TextView) view.findViewById(R.id.fragment_notes_list_prompt);
        mAddNoteButton = (ImageButton) view.findViewById(R.id.fragment_notes_list_add);
        mNotesRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notes_list_recycler_view);
        //Underline ToDoTitleTextView
        mToDoTitleTextView.setPaintFlags(mToDoTitleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mToDoTitleTextView.setText("To-Do List");
        //setLayoutManager to Linear for RecyclerView
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /**
         * Add stored tasks to NotesSingleton
         */
        readDB();
        //Configure UserInterface
        UpdateUI();
        //Add listener to add button
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesItem note = new NotesItem();
                NotesSingleton.get(getActivity()).addNotesItem(note);
                Intent intent = SingleNoteActivity.newIntent(getActivity(), note.getId());
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Reads database and inserts all data into List<NotesItem> in NotesSingleton class
     */
    private void readDB() {
        //cursor contains all data in the database
        Cursor cursor = dbSchema.storedTasks(db);
        NotesSingleton notesSingleton = NotesSingleton.get(getActivity());

        NotesItem notesItem;

        //Checks if cursor has any data
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            do{
                //Creates new NotesItem object with task stored in databases
                notesItem = new NotesItem();
                notesItem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex("ID"))));
                notesItem.setDescription(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));

                //Converts date that is stored in the database as text into Date object
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {

                    notesItem.setDate(formatter.parse(cursor.getString(cursor.getColumnIndex("DATE"))));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Adds new NotesItem into List in NotesSingleton class
                notesSingleton.addNotesItem(notesItem);
                //continues until cursor is empty
            }while(cursor.moveToNext());
            //close cursor
            cursor.close();
        }
        //close database
        db.close();
    }

    private void UpdateUI() {
        NotesSingleton notesSingleton = NotesSingleton.get(getActivity());
        List<NotesItem> notes = notesSingleton.getNotesItems();

        //TODO figure out how to implement notifyDataSetChanged() without
        //TODO losing list
//        if (mNoteAdapter == null) {
        mNoteAdapter = new NoteAdapter(notes);
        mNotesRecyclerView.setAdapter(mNoteAdapter);
//        } else {
//            mNoteAdapter.notifyDataSetChanged();
//        }

        //Update RecyclerView and TextView depending if the notes are empty
        if (notes.isEmpty()) {
            //display TextView prompt to add item
            mNotesRecyclerView.setVisibility(View.GONE);
            mPromptTextView.setVisibility(View.VISIBLE);
        }
        else {
            //display notes on RecyclerView
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            mPromptTextView.setVisibility(View.GONE);
        }
    }

    /** CLASS
     * View Holder for each note item
     */
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private NotesItem mNote;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageButton mDeleteButton;

        SQLiteDatabase db;
        NoteDbSchema dbSchema;

        //Constructor sets up class by wiring widgets
        public NoteHolder(View itemView) {
            super(itemView);

            /**
             * initialize database
             */
            dbSchema = new NoteDbSchema(getActivity().getApplicationContext());
            db = dbSchema.getReadableDatabase();

            //Set onClick for the RecyclerView item
            itemView.setOnClickListener(this);
            //Wire up Widget Views for each RecyclerView Item
            mTitleTextView = (TextView) itemView.findViewById(R.id.notes_list_item_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.notes_list_item_date);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.notes_list_item_delete_button);
        }


        //Gets called in NoteAdapter.onBindViewHolder() in order to bind the
        //proper values to to Widget Views to a particular note
        public void bindNote(NotesItem note) {
            mNote = note;
            //Assign values to widgets
            mTitleTextView.setText(mNote.getDescription());
            mDateTextView.setText(formattedDate(mNote.getDate()));
            //Wire up Delete button functionality
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /**
                     * delete row from database
                     */
                    dbSchema.deleteTask(mNote);

                    NotesSingleton.get(getActivity()).deleteNotesItem(mNote);
                    UpdateUI();
                }
            });
        }

        private String formattedDate(Date date) {
//            if (date == null)
//                return null;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String formatD = format.format(date);
            return formatD;
        }

        //Implementation of when the certain RecyclerView View is clicked
        @Override
        public void onClick(View view) {
            //pass mNote's Id to activity
            Intent intent = SingleNoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }
    }

    /** CLASS
     * Adapter used to help create the RecyclerView
     */
    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<NotesItem> mNotes;

        //Constructor class assigns list to local class listModel
        public NoteAdapter(List<NotesItem> notes) {
            mNotes = notes;
        }

        //Set up the layout of the RecyclerView items
        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            //Wire Up layout of ech item in recyclerView
            View view = inflater.inflate(R.layout.notes_list_item, parent, false);
            return new NoteHolder(view);
        }

        //Bind the viewHolder
        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            NotesItem note = mNotes.get(position);

            //Set the values of the RecyclerView Item to those of the
            //particular note at the time
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
