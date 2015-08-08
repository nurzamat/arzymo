package org.ananasit.arzymo.util;

import android.widget.AbsListView;

import org.ananasit.arzymo.MyPostsActivity;
import org.ananasit.arzymo.PostsActivity;

/**
 * Created by nurzamat on 8/7/15.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private PostsActivity postsActivity;
    private MyPostsActivity myPostsActivity;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    public EndlessScrollListener(PostsActivity _postsActivity) {
        this.postsActivity = _postsActivity;
    }

    public EndlessScrollListener(PostsActivity _postsActivity, int visibleThreshold) {
        this.postsActivity = _postsActivity;
        this.myPostsActivity = null;
        this.visibleThreshold = visibleThreshold;
    }
    public EndlessScrollListener(MyPostsActivity _myPostsActivity, int visibleThreshold) {
        this.myPostsActivity = _myPostsActivity;
        this.postsActivity = null;
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            if (postsActivity != null && postsActivity.next > 0)
                postsActivity.VolleyRequest(ApiHelper.getCategoryPostsUrl(postsActivity.next));
            if (myPostsActivity != null && myPostsActivity.next > 0)
                myPostsActivity.VolleyRequest(ApiHelper.getMyPostsUrl(myPostsActivity.user_id, myPostsActivity.next));
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
