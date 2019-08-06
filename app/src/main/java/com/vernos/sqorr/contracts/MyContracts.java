package com.vernos.sqorr.contracts;

import com.vernos.sqorr.pojos.DummyContent;

/**
 * Created by Kalyan on 27/6/2019.
 */
public interface MyContracts {

    interface View {
        void initView();
        void setViewData(String data);
//        void onListFragmentInteraction(DummyContent.DummyItem item);

    }

    interface Model {
        String getData();
    }

    interface Presenter {
        void onClick(android.view.View view);
    }

}
