package hemath.com.pagination.LoadMoreOnScroll;

import android.content.*;
import android.support.v7.widget.*;
import android.util.*;

import static android.widget.NumberPicker.OnScrollListener.*;


public class InfiniteScrollProvider
{
    Context context;

    private RecyclerView recyclerView;
    private boolean isLoading = false;
    private OnLoadMoreListener onLoadMoreListener;
    private ScrollPosChanged onScrollPosChanged;
    private RecyclerView.LayoutManager layoutManager;
    private int lastVisibleItem;
    private int totalItemCount;
    private int previousItemCount = 0;
    private static final int THRESHOLD = 3;

    private int scrollStart = 0;


    public void attach(RecyclerView recyclerView,OnLoadMoreListener onLoadMoreListener) {
        this.recyclerView = recyclerView;
        this.onLoadMoreListener = onLoadMoreListener;
        layoutManager =recyclerView.getLayoutManager();
        setInfiniteScrollGrid(layoutManager);
    }


    public void attach(RecyclerView recyclerView,ScrollPosChanged ScrollPosChanged) {
        this.recyclerView = recyclerView;
        this.onScrollPosChanged = ScrollPosChanged;
        layoutManager =recyclerView.getLayoutManager();
        setInfiniteScrollGrid(layoutManager);
    }

    private void setInfiniteScrollGrid(final RecyclerView.LayoutManager layoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy)
            {


//                if(scrollStart <= 50){
//                    scrollStart++;
//                    onScrollPosChanged.ScrollPosChanged(0);
//                }

                onScrollPosChanged.ScrollPosChanged(0,false);

                totalItemCount = layoutManager.getItemCount();

                if (previousItemCount > totalItemCount)
                {
                    previousItemCount = totalItemCount - THRESHOLD;
                }
                if (layoutManager instanceof GridLayoutManager)
                {
                    lastVisibleItem = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof LinearLayoutManager)
                {
                    lastVisibleItem = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof StaggeredGridLayoutManager)
                {
                    StaggeredGridLayoutManager staggeredGridLayoutManager=(StaggeredGridLayoutManager) layoutManager;
                    int spanCount=staggeredGridLayoutManager.getSpanCount();
                    int[] ids=new int[spanCount];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(ids);
                    int max=ids[0];
                    for (int i = 1; i < ids.length; i++) {
                        if (ids[1]>max){
                            max=ids[1];
                        }
                    }
                    lastVisibleItem=max;
                }
                if (totalItemCount > THRESHOLD) {
                    if (previousItemCount <= totalItemCount && isLoading) {
                        isLoading = false;
                        Log.i("InfiniteScroll", "Data fetched");
                    }
                    if (!isLoading && (lastVisibleItem > (totalItemCount - THRESHOLD)) && totalItemCount > previousItemCount) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        Log.i("InfiniteScroll", "End Of List");
                        isLoading = true;
                        previousItemCount = totalItemCount;
                    }
                }
                super.onScrolled(recyclerView, dx, dy);


               // super.onScrollStateChanged(recyclerView, );
            }


            public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                 if(newState == SCROLL_STATE_IDLE){
                     scrollStart = 0;
                     onScrollPosChanged.ScrollPosChanged(0, true);
                 }

            }
        });
    }

}
