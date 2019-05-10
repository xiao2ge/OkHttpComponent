package com.cekong.panran.okhttpcomponent;

import java.util.List;

/**
 * TestBean
 *
 * @author LR
 * @date 2019/5/9 8:46
 */
public class TestBean {

    /**
     * result : 0
     * message :
     * data : [{"id":10,"title":"磐然测控","image":"85bc50af-bb0a-4a7f-9249-92aa553b06ea.png","url":"about.html"},{"id":16,"title":"PR203/PR205系列温湿度场巡检仪","image":"70cf0df4-c52a-47ee-8dfd-75cd5e889780.png","url":"product:16,PR203PR205系列温湿度场巡检仪.html"},{"id":23,"title":"PR293系列纳伏微欧测温仪","image":"35366bb5-54ae-4b0e-b522-8d853eff4174.png","url":"product:23,PR293系列纳伏微欧测温仪.html"},{"id":24,"title":"PR710系列手持式精密数字温度计","image":"0f170a9e-fddb-4864-a123-36d800476b19.png","url":"product:21,pr710.html"},{"id":25,"title":"PR720系列工业数字温度计","image":"6e4e78ac-e011-4ca4-a04d-3e6965cd43ca.png","url":"product:24,PR720系列工业数字温度计.html"},{"id":26,"title":"ZRJ系列智能化热工仪表检定系统","image":"b48c43b2-82b3-4924-8cbc-71430a423ea3.png","url":"product:22,zrj.html"}]
     */

    private int result;
    private String message;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * title : 磐然测控
         * image : 85bc50af-bb0a-4a7f-9249-92aa553b06ea.png
         * url : about.html
         */

        private int id;
        private String title;
        private String image;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
