package com.boredream.bdcodehelper.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;

/**
 * 加载更多装饰适配器,用于包装普通RecyclerView.Adapter增添一个加载更多功能
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {
    public static final int STATUS_NONE = 0;
    public static final int STATUS_HAVE_MORE = 1;
    public static final int STATUS_LOADED_ALL = 2;
    private int status = STATUS_NONE;

    private int ITEM_VIEW_TYPE_FOOTER = 0x10002;

    private boolean isLoading = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Drawable mLoadMoreProgressDrawable;

    public LoadMoreAdapter(RecyclerView recyclerView,
                           RecyclerView.Adapter adapter,
                           OnLoadMoreListener onLoadMoreListener) {
        this(recyclerView, adapter, onLoadMoreListener, null);
    }

    public LoadMoreAdapter(RecyclerView recyclerView,
                           RecyclerView.Adapter adapter,
                           OnLoadMoreListener onLoadMoreListener,
                           Drawable loadMoreProgressDrawable) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mOnLoadMoreListener = onLoadMoreListener;
        mLoadMoreProgressDrawable = loadMoreProgressDrawable;
        setScrollListener();
    }

    public RecyclerView.Adapter getSrcAdapter() {
        return mAdapter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        isLoading = false;
        this.status = status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View loadMore = inflater.inflate(R.layout.footer_progress, parent, false);
            LoadMoreViewHolder holder = new LoadMoreViewHolder(loadMore);
            return holder;
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            handleFooter((LoadMoreViewHolder) holder);
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    private void handleFooter(final LoadMoreViewHolder holder) {
        if (mLoadMoreProgressDrawable != null) {
            holder.pb_footer_progress.setIndeterminateDrawable(mLoadMoreProgressDrawable);
        }

        // set footer full span
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (mAdapter.getItemViewType(position) == ITEM_VIEW_TYPE_FOOTER) {
                        spanSize = manager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }

        // check status
        switch (status) {
            case STATUS_HAVE_MORE:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.GONE);
                holder.pb_footer_progress.setVisibility(View.VISIBLE);
                break;
            case STATUS_LOADED_ALL:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.VISIBLE);
                holder.pb_footer_progress.setVisibility(View.GONE);
                break;
            case STATUS_NONE:
            default:
                holder.itemView.setVisibility(View.GONE);
                break;
        }
    }

    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int pastVisibleItems = -1;
                    int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    int totalItemCount = staggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        triggerLoadMore();
                    }
                } else if (layoutManager instanceof LinearLayoutManager) {
                    // LinearLayoutManager and GridLayoutManager
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        triggerLoadMore();
                    }
                }
            }
        });
    }

    private synchronized void triggerLoadMore() {
        // block duplicate
        if (isLoading) {
            return;
        }

        // check status
        if (status != STATUS_HAVE_MORE) {
            return;
        }

        Log.i("DDD", "load more");

        isLoading = true;
        mOnLoadMoreListener.onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAdapter.getItemCount()) {
            return ITEM_VIEW_TYPE_FOOTER;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    public void notifyFooterChanged() {
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar pb_footer_progress;
        public TextView tv_footer_progress;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            pb_footer_progress = (ProgressBar) itemView.findViewById(R.id.pb_footer_progress);
            tv_footer_progress = (TextView) itemView.findViewById(R.id.tv_footer_progress);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
