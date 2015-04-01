package com.xy.tickets.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.umeng.analytics.MobclickAgent;
import com.xy.tickets.MainActivity;
import com.xy.tickets.R;
import com.xy.tickets.db.SMSMessageModel;

import java.util.ArrayList;
import java.util.List;

public class TicketListFragment extends Fragment implements Refreshable{

	public final static String LOGTAG = "CommentListFragment";

    private static final int INITIAL_DELAY_MILLIS = 500;

    private ListView listview;

    private TextView loaddataTxt;

    private MyExpandableListItemAdapter mAdapter;

    private List<SMSMessageModel> contents = new ArrayList<SMSMessageModel>();

    private int smsAmount;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(LOGTAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_comment_list, container,
				false);

        loaddataTxt = (TextView) v.findViewById(R.id.load_data_txt);

        // Set a listener to be invoked when the list should be refreshed.
        listview = (ListView) v.findViewById(R.id.comment_list);

        // You can also just use mPullRefreshListFragment.getListView()
        mAdapter = new MyExpandableListItemAdapter(getActivity());

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mAdapter);
        alphaInAnimationAdapter.setAbsListView(listview);

        assert alphaInAnimationAdapter.getViewAnimator() != null;
        alphaInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listview.setAdapter(alphaInAnimationAdapter);

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        listview.setAdapter(mAdapter);

        reloadComments();

		return v;
	}

    private void reloadComments(){
        ((MainActivity) getActivity()).getService().loadSMSFromInbox(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    mAdapter.add(0, (SMSMessageModel) msg.obj);
                    if (msg.arg1 > 0) {
                        loaddataTxt.setText(getActivity().getString(R.string.load_data, msg.arg1, smsAmount));
                    }
                    mAdapter.notifyDataSetChanged();
                } else if (msg.what == 1) {
                    smsAmount = msg.arg1;
                } else if (msg.what == 3) {
                    loaddataTxt.setText(getActivity().getString(R.string.load_data, msg.arg1, smsAmount));
                }
            }
        });
    }

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(LOGTAG);
	}

    @Override
    public void refreshTitle(Activity activity) {
        ((MainActivity)activity).setRoot(true);
        ((MainActivity)activity).setTitleTxt(R.string.app_name);
        ((MainActivity)activity).setTitleBtn(true, false, null, null);

    }

    private class MyExpandableListItemAdapter extends ExpandableListItemAdapter<SMSMessageModel> {

        private final LayoutInflater mInflater;

        /**
         * Creates a new ExpandableListItemAdapter with the specified list, or an empty list if
         * items == null.*/
        public MyExpandableListItemAdapter(final Context context) {
            super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content);

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getTitleView(final int position, final View convertView, final ViewGroup parent) {
            LinearLayout tv = (LinearLayout) convertView;
            if (tv == null) {
                tv = (LinearLayout) mInflater.inflate(R.layout.ticket_list_item,
                        null);
            }
            TitleViewHolder holder = (TitleViewHolder) tv.getTag();
            if (holder == null) {
                holder = new TitleViewHolder(tv);
                tv.setTag(holder);
            }
            SMSMessageModel sms = getItem(position);
            switch (sms.getTicketType()) {
            case MOVIE:
            	tv.setBackgroundColor(Color.parseColor("#B770AF"));
            	holder.icon.setImageResource(R.drawable.index_icon5);
                break;
            case GROUPBUY:
            	tv.setBackgroundColor(Color.parseColor("#ECC67A"));
            	holder.icon.setImageResource(R.drawable.index_icon3);
                break;
            case FLIGHT:
            	tv.setBackgroundColor(Color.parseColor("#43A8CF"));
            	holder.icon.setImageResource(R.drawable.index_icon);
                break;
            case TRAIN:
            	tv.setBackgroundColor(Color.parseColor("#D25452"));
            	holder.icon.setImageResource(R.drawable.index_icon2);
                break;
            case HOTEL:
            	tv.setBackgroundColor(Color.parseColor("#91C551"));
            	holder.icon.setImageResource(R.drawable.index_icon4);
                break;
			}
            holder.title.setText(sms.getTitle());
            holder.subTitle.setText(sms.getSubTitle());

            return tv;
        }

        @Override
        public View getContentView(final int position, final View convertView, final ViewGroup parent) {
            LinearLayout panel = null;

            SMSMessageModel sms = getItem(position);

            switch (sms.getTicketType()) {
                case MOVIE:
                    panel = (LinearLayout) mInflater.inflate(R.layout.sms_movie_panel,
                            null);
                    getMoiveView(panel, sms);
                    break;
                case GROUPBUY:
                    panel = (LinearLayout) mInflater.inflate(R.layout.sms_group_buy_panel,
                            null);
                    getGroupView(panel, sms);
                    break;
                case FLIGHT:
                    panel = (LinearLayout) mInflater.inflate(R.layout.sms_flight_panel,
                            null);
                    getFlightView(panel, sms);
                    break;
                case TRAIN:
                    panel = (LinearLayout) mInflater.inflate(R.layout.sms_train_panel,
                            null);
                    getTrainView(panel, sms);
                    break;
                case HOTEL:
                    panel = (LinearLayout) mInflater.inflate(R.layout.sms_hotel_panel,
                            null);
                    getHotelView(panel, sms);
                    break;
            }

            return panel;
        }

    }

    private void getMoiveView(View view, SMSMessageModel sms){
        TextView passwordTxt = (TextView) view.findViewById(R.id.ticket_password_txt);
        passwordTxt.setText(sms.getPassword());

        TextView ticketRoomTxt = (TextView) view.findViewById(R.id.ticket_room_txt);
        ticketRoomTxt.setText(sms.getRoom());

        TextView ticketSeatTxt = (TextView) view.findViewById(R.id.ticket_seat_txt);
        ticketSeatTxt.setText(sms.getSeat());

        TextView ticketAddressTxt = (TextView) view.findViewById(R.id.ticket_address_txt);
        ticketAddressTxt.setText(sms.getBusinessAddress());

        TextView ticketSourceTxt = (TextView) view.findViewById(R.id.ticket_source_txt);
        ticketSourceTxt.setText(sms.getSender());

    }

    private void getGroupView(View view, SMSMessageModel sms){
        TextView passwordTxt = (TextView) view.findViewById(R.id.ticket_password_txt);
        passwordTxt.setText(sms.getPassword());

        TextView ticketValidTimeTxt = (TextView) view.findViewById(R.id.ticket_valid_time_txt);
        ticketValidTimeTxt.setText(sms.getAvailableDate());

        TextView ticketPhoneTxt = (TextView) view.findViewById(R.id.ticket_business_phone_txt);
        ticketPhoneTxt.setText(sms.getBusinessPhoneNumber());

        TextView ticketAddressTxt = (TextView) view.findViewById(R.id.ticket_business_address_txt);
        ticketAddressTxt.setText(sms.getBusinessAddress());

        TextView ticketSourceTxt = (TextView) view.findViewById(R.id.ticket_source_txt);
        ticketSourceTxt.setText(sms.getSender());

    }

    private void getTrainView(View view, SMSMessageModel sms){
        TextView fromStationTxt = (TextView) view.findViewById(R.id.ticket_from_station_txt);
        fromStationTxt.setText(sms.getFromStation());

        TextView fromDateTxt = (TextView) view.findViewById(R.id.ticket_train_from_date_txt);
        fromDateTxt.setText(sms.getFromDate());

        TextView fromTimeTxt = (TextView) view.findViewById(R.id.ticket_train_from_time_txt);
        fromTimeTxt.setText(sms.getFromTime());

        TextView toStationTxt = (TextView) view.findViewById(R.id.ticket_to_station_txt);
        toStationTxt.setText(sms.getToStation());

        TextView toDateTxt = (TextView) view.findViewById(R.id.ticket_train_to_date_txt);
        toDateTxt.setText(sms.getToDate());

        TextView toTimeTxt = (TextView) view.findViewById(R.id.ticket_train_to_time_txt);
        toTimeTxt.setText(sms.getToTime());

        TextView roomNoTxt = (TextView) view.findViewById(R.id.ticket_train_room_no_txt);
        roomNoTxt.setText(sms.getRoomNo());

        TextView seatNoTxt = (TextView) view.findViewById(R.id.ticket_train_seat_no_txt);
        seatNoTxt.setText(sms.getSeatNo());

        TextView ticketSourceTxt = (TextView) view.findViewById(R.id.ticket_source_txt);
        ticketSourceTxt.setText(sms.getSender());
    }

    private void getFlightView(View view, SMSMessageModel sms){
        TextView fromCityTxt = (TextView) view.findViewById(R.id.ticket_fromCity_txt);
        fromCityTxt.setText(sms.getFromCity());

        TextView fromAirportTxt = (TextView) view.findViewById(R.id.ticket_fromAirport_txt);
        fromAirportTxt.setText(sms.getFromAirport());

        TextView fromDateTxt = (TextView) view.findViewById(R.id.ticket_fromDate_txt);
        fromDateTxt.setText(sms.getFromDate());

        TextView fromTimeTxt = (TextView) view.findViewById(R.id.ticket_fromTime_txt);
        fromTimeTxt.setText(sms.getFromTime());

        TextView toCityTxt = (TextView) view.findViewById(R.id.ticket_toCity_txt);
        toCityTxt.setText(sms.getToCity());

        TextView toAirportTxt = (TextView) view.findViewById(R.id.ticket_toAirport_txt);
        toAirportTxt.setText(sms.getToAirport());

        TextView toDateTxt = (TextView) view.findViewById(R.id.ticket_toDate_txt);
        toDateTxt.setText(sms.getToDate());

        TextView toTimeTxt = (TextView) view.findViewById(R.id.ticket_toTime_txt);
        toTimeTxt.setText(sms.getToTime());

        TextView airlineTxt = (TextView) view.findViewById(R.id.ticket_airline_txt);
        airlineTxt.setText(sms.getAirline());

        TextView ticketSourceTxt = (TextView) view.findViewById(R.id.ticket_source_txt);
        ticketSourceTxt.setText(sms.getSender());
    }

    private void getHotelView(View view, SMSMessageModel sms){
        TextView roomTypeTxt = (TextView) view.findViewById(R.id.ticket_roomType_txt);
        roomTypeTxt.setText(sms.getRoomType());

        TextView roomAmountTxt = (TextView) view.findViewById(R.id.ticket_roomAmount_txt);
        roomAmountTxt.setText(sms.getRoomAmount());

        TextView nightAmountTxt = (TextView) view.findViewById(R.id.ticket_nightAmount_txt);
        nightAmountTxt.setText(sms.getNightAmount());

        TextView phoneTxt = (TextView) view.findViewById(R.id.ticket_hotel_phone_txt);
        phoneTxt.setText(sms.getBusinessPhoneNumber());

        TextView addressTxt = (TextView) view.findViewById(R.id.ticket_hotel_address_txt);
        addressTxt.setText(sms.getBusinessAddress());

        TextView ticketSourceTxt = (TextView) view.findViewById(R.id.ticket_source_txt);
        ticketSourceTxt.setText(sms.getSender());
    }

    class TitleViewHolder {
        TextView title = null;
        TextView subTitle = null;
        ImageView icon = null;
        ImageView infoIcon = null;

        TitleViewHolder(View base) {
            this.title = (TextView) base.findViewById(R.id.ticket_title_txt);
            this.subTitle = (TextView) base.findViewById(R.id.ticket_subtitle_txt);
            this.icon = (ImageView) base.findViewById(R.id.ticket_icon_img);
            this.infoIcon = (ImageView) base.findViewById(R.id.ticket_info_icon_img);
        }
    }


}
