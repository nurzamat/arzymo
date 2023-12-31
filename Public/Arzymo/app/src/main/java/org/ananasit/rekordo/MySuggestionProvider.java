package org.ananasit.rekordo;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by nurzamat on 2/10/15.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "org.ananasit.rekordo.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
