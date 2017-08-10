package com.boredream.bdcodehelper.entity;

/**
 * 页数索引, 多页加载时使用
 */
public class PageIndex {

    /**
     * 起始页
     */
    private int startPage;

    /**
     * 请求加载的新页数
     */
    private int newPage;

    /**
     * 当前已加载的页数
     */
    private int currentPage;

    /**
     * 每页加载的数量
     */
    private int countPerPage;

    public int getStartPage() {
        return startPage;
    }

    /**
     * 页数索引
     *
     * @param startPage 起始页, 通常是 0 或 1
     */
    public PageIndex(int startPage, int countPerPage) {
        this.startPage = startPage;
        this.countPerPage = countPerPage;
        init();
    }

    /**
     * 重置初始化
     */
    public void init() {
        this.currentPage = startPage;
    }

    /**
     * 起始页
     *
     * @return 起始页
     */
    public int toStartPage() {
        newPage = startPage;
        return newPage;
    }

    /**
     * 下一页
     *
     * @return 当前页+1作为下一页
     */
    public int toNextPage() {
        newPage = currentPage + 1;
        return newPage;
    }

    // TODO: 2017/8/10
//    /**
//     * 数据获取成功, 设置返回结果
//     *
//     * @param adapter
//     * @param currentList 当前已有数据
//     * @param newList     请求获取的新数据
//     * @param <T>
//     */
//    public <T> void setResponse(LoadMoreAdapter adapter, List<T> currentList, List<T> newList) {
//        // 更新当前页数
//        currentPage = newPage;
//
//        // 如果当前页为起始页, 则清空数据
//        if (currentPage == startPage) {
//            currentList.clear();
//        }
//
//        if (newList != null && newList.size() > 0) {
//            // 添加数据
//            currentList.addAll(newList);
//
//            // 判断是否已经加载完全部数据，设置Adapter对应状态
//            adapter.setStatus(newList.size() == countPerPage
//                    ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);
//        } else {
//            adapter.setStatus(LoadMoreAdapter.STATUS_LOADED_ALL);
//        }
//
//        // 更新UI
//        adapter.notifyDataSetChanged();
//    }

}
