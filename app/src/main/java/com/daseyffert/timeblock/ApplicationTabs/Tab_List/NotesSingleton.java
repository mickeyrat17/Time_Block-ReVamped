package com.daseyffert.timeblock.ApplicationTabs.Tab_List;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Daniel on 12/23/2015.
 * Singleton is created to allow for only one instance of
 * a list of items of notes to be created
 */
public class NotesSingleton {

    private static NotesSingleton sNotesSingleton;
    private List<NotesItem> mNotesItems;


    public static NotesSingleton get(Context context) {
        if (sNotesSingleton == null) {
            sNotesSingleton = new NotesSingleton(context);
        }

        return sNotesSingleton;
    }

    //Private Constructor: other method cannot
    //call this without bypasing get(Context)
    private NotesSingleton(Context context) {
        mNotesItems = new ArrayList<>();

        //Testing list
//        for (int i = 0; i < 7; i++) {
//            NotesItem note = new NotesItem();
//            note.setTitle("Note #" + i);
//            note.setDate(new Date());
//            mNotesItems.add(note);
//        }
    }

    //Add a new note to list
    public void addNotesItem(NotesItem note) {
        mNotesItems.add(note);
    }

    //Remove note from list
    public void deleteNotesItem(NotesItem notes){
        mNotesItems.remove(notes);
    }

    //Retrieve the list of Notes
    public List<NotesItem> getNotesItems() {
        return mNotesItems;
    }

    //Retrieve a particular Note id found
    public NotesItem getNotesItem(UUID id) {
        //Iterate through list check for
        //particular note
        for (NotesItem note : mNotesItems)
            if (note.getId().equals(id))
                return note;
        return null;
    }
}
