package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.adapter.BGASwipeRecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.engine.DataEngine;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGARefreshLayout;
import cn.bingoogolapple.acvp.refreshlayout.widget.Divider;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class BGASwipeRecyclerViewDemoActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private BGASwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private RecyclerView mDataRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        initRefreshLayout();
        initRecyclerView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setCustomHeaderView(DataEngine.getCustomHeaderOrFooterView(this));
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
    }

    private void initRecyclerView() {
        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);
        mDataRv.addItemDecoration(new Divider(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mAdapter = new BGASwipeRecyclerViewAdapter(this);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mDatas.addAll(0, DataEngine.loadNewData());
                mAdapter.setDatas(mDatas);
            }
        }.execute();
    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mAdapter.addDatas(DataEngine.loadMoreData());
            }
        }.execute();
    }

    @Override
    public void onRVItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目 " + mAdapter.getItemMode(position).mTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目 " + mAdapter.getItemMode(position).mTitle, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_bgaswipe_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(View v, int position) {
        if (v.getId() == R.id.tv_item_bgaswipe_delete) {
            Toast.makeText(this, "长按了删除 " + mAdapter.getItemMode(position).mTitle, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}