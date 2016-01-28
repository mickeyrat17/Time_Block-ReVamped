package com.daseyffert.timeblock.ApplicationTabs.Tab_List.SingleNote;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.daseyffert.timeblock.SingleFragmentActivity;

import java.util.UUID;

/**
 * Created by Daniel on 12/23/2015.
 * Activity that displays notes
 */
public class SingleNoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID = "package com.daseyffert.timeblock.ApplicationTabs.Tab3.SingleNote.note_id";

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, SingleNoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;

    }

    @Override
    protected Fragment createFragment() {
        //
        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        return SingleNoteFragment.newInstance(noteId);
    }
}
