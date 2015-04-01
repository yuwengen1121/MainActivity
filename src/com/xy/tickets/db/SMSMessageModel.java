package com.xy.tickets.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.xy.tickets.util.DateUtils;
import com.xy.tickets.util.Utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Activity for banner slider from content or others.
 * 
 * @author chenyu
 * @since 1.0
 */
@DatabaseTable(tableName = "sms")
public class SMSMessageModel implements Serializable{

	@DatabaseField(id = true)
	private Long id;

    /*************SMS source field**************/
    @DatabaseField
    private String phoneNumber;  //短信接入码

    @DatabaseField
    private String content;//短信内容

    @DatabaseField
    private String smsCenterNum;//接收短信的短信中心号码

    @DatabaseField
    private String scendId;//场景id

    @DatabaseField
    private TicketType ticketType;//票票类型

    @DatabaseField
    private ButtonType button1;//按钮1

    @DatabaseField
    private ButtonType button2;//按钮2

    /************Shared Fields**************/
	@DatabaseField
    private String title;//标题

    @DatabaseField
    private String subTitle;//副标题

    @DatabaseField
    private Date dueToDate;//截止时间

    @DatabaseField
    private Date usedDate;//使用时间

    @DatabaseField
    private boolean used;//是否已使用

    @DatabaseField
    private String sender;//短信发送来源

    /***********Group Buying***********/
    @DatabaseField
    private String password;//密码

    @DatabaseField
    private String availableDate;//有效时间

    @DatabaseField
    private String businessPhoneNumber;//商户电话

    @DatabaseField
    private String businessAddress;//商户地址

    /***********Movie************/
    @DatabaseField
    private String room;//放映厅

    @DatabaseField
    private String seat;//座位

    @DatabaseField
    private String seeAdd;//观影地点

    @DatabaseField
    private String ticketAdd;//取票地点

    /********Flight*********/
    @DatabaseField
    private String fromCity;//出发城市

    @DatabaseField
    private String fromAirport;//出发机场

    @DatabaseField
    private String fromDate;//出发日期

    @DatabaseField
    private String fromTime;//出发时间

    @DatabaseField
    private String toCity;//到达城市

    @DatabaseField
    private String toAirport;//到达机场

    @DatabaseField
    private String toDate;//到达日期

    @DatabaseField
    private String toTime;//到达时间

    @DatabaseField
    private String airline;//航空公司

    /***********Hotel************/
    @DatabaseField
    private String roomType;//房间类型

    @DatabaseField
    private String roomAmount;//房间数目

    @DatabaseField
    private String nightAmount;//入住夜数

    /***********Train************/
    @DatabaseField
    private String trainNo;//车次

    @DatabaseField
    private String fromStation;//出发地

    @DatabaseField
    private String toStation;//方向

    @DatabaseField
    private String roomNo;//车厢

    @DatabaseField
    private String seatNo;//座位

    @DatabaseField
    private String seatType;//席别

    @DatabaseField
    private boolean deleteFlag;

    @DatabaseField
    private Date createdAt;

    public SMSMessageModel(){
    	
    }

    public static SMSMessageModel getSMSMessageModel(String phone, String content, Date createdAt, Map<String, Object> map){
        SMSMessageModel sms = new SMSMessageModel();
        sms.setPhoneNumber(phone);
        sms.setContent(content);
        sms.setCreatedAt(createdAt);
        String sceneIdStr = (String) map.get("title_num");
        String prefix = "";
        if(sceneIdStr.startsWith("15002")){
            //电影票订票
            prefix = "ticket_book_";
            sms.setTicketType(TicketType.MOVIE);
            sms.setSender((String) map.get(prefix + "channelname"));
            sms.setTitle(map.get(prefix + "movie") + " " + map.get(prefix + "number") + "张");
            sms.setSubTitle(map.get(prefix + "date") + " " + map.get(prefix + "time") + "放映");
            sms.setPassword((String) map.get(prefix + "password"));
            String room = (String) map.get(prefix + "room");
            if(room != null) {
                sms.setRoom(room);
            }else {
                sms.setRoom("--");
            }
            sms.setSeat((String) map.get(prefix + "seat_arr"));
            String seeingAdd = (String) map.get(prefix + "theatrename");
            String ticketAdd = (String) map.get(prefix + "takeadd");
            String add = (String) map.get(prefix + "add");

            sms.setSeeAdd(seeingAdd);
            sms.setTicketAdd(ticketAdd);
            sms.setBusinessAddress(add);
            sms.setButton1(ButtonType.MAP);

            sms.setDueToDate(DateUtils.defaultParse((String) map.get(prefix + "date")));
        }else if(sceneIdStr.startsWith("05015")){
            //酒店订单
            prefix = "travel_hotelorder_";
            sms.setTicketType(TicketType.HOTEL);
            sms.setSender((String) map.get(prefix + "channelname"));
            sms.setTitle(map.get(prefix + "hotelname") + "" + map.get(prefix + "roomtype"));
            sms.setSubTitle(map.get(prefix + "checkindate") + " 24点前入住");
            sms.setRoomType((String) map.get(prefix + "roomtype"));
            sms.setRoomAmount((String) map.get(prefix + "roomnum"));
            sms.setNightAmount((String) map.get(prefix + "nightnum"));
            sms.setBusinessAddress((String) map.get(prefix + "hoteladd"));
            sms.setBusinessPhoneNumber((String) map.get(prefix + "hotelphonenum"));
            sms.setButton1(ButtonType.MAP);
            sms.setButton2(ButtonType.PHONEH);

            sms.setDueToDate(DateUtils.defaultParse((String) map.get(prefix + "checkindate")));
        }else if(sceneIdStr.startsWith("08000")){
            //团购
            prefix = "groupbuying_details_";
            sms.setTicketType(TicketType.GROUPBUY);
            sms.setSender((String) map.get(prefix + "groupbuyingname"));
            sms.setTitle((String) map.get(prefix + "commodityname"));
            sms.setSubTitle(map.get(prefix + "quantity") + "" + map.get(prefix + "unit"));
            sms.setAvailableDate(map.get(prefix + "expdatestart") + "" + map.get(prefix + "expdateend"));
            sms.setBusinessAddress((String) map.get(prefix + "hoteladd"));
            sms.setBusinessPhoneNumber((String) map.get(prefix + "hotelphonenum"));
            sms.setButton1(ButtonType.MAP);
            sms.setButton2(ButtonType.PHONEB);

            sms.setDueToDate(DateUtils.defaultParse((String) map.get(prefix + "checkindate")));
        }else if(sceneIdStr.startsWith("05001") || sceneIdStr.startsWith("05003")){
            //机票
            if(sceneIdStr.startsWith("05001")) {
                prefix = "travel_issueairticket_";
            }else {
                prefix = "travel_bookairticket_";
            }
            sms.setAirline(getAirlineFromSceneId(sceneIdStr));
            sms.setTicketType(TicketType.FLIGHT);
            sms.setSender((String) map.get(prefix + "channelname"));
            String[] addresses = (String[]) (map.get(prefix + "from_place_arr_complex"));
            String[] addresses2 = (String[]) (map.get(prefix + "to_place_arr_complex"));
            if(addresses != null && addresses.length > 0 && addresses2 != null && addresses2.length > 0) {
                String[] arr = addresses[0].split(";");
                String[] arr2 = addresses2[0].split(";");
                if(arr.length > 0) {
                    sms.setFromCity(arr[0]);
                }else {
                    sms.setFromCity("--");
                }
                if(arr.length > 1) {
                    sms.setFromAirport(arr[1]);
                }else {
                    sms.setFromAirport("--");
                }
                if(arr2.length > 0) {
                    sms.setToCity(arr2[0]);
                }else {
                    sms.setToCity("--");
                }
                if(arr2.length > 1) {
                    sms.setToAirport(arr2[1]);
                }else {
                    sms.setToAirport("--");
                }
                sms.setTitle(map.get(prefix + "flightnum_arr") + " " + sms.getFromCity() + "-" + sms.getToCity());
                sms.setFromDate((String) map.get(prefix + "departuredate_arr"));
                sms.setToDate((String) map.get(prefix + "arrivaldate_arr"));
                sms.setFromTime((String) map.get(prefix + "departuretime_arr"));
                sms.setToTime((String) map.get(prefix + "arrivaltime_arr"));
                sms.setSubTitle(sms.getFromDate() + " " + sms.getFromTime() + " 起飞");
            }
            sms.setButton1(ButtonType.FLIGHTSTATUS);

            sms.setDueToDate(DateUtils.defaultParse((String) map.get(prefix + "departuredate_arr")));
        }else if(sceneIdStr.startsWith("05008") || sceneIdStr.startsWith("05014")){
            //TODO:火车票
            if(sceneIdStr.startsWith("05008")) {
                prefix = "travel_trainticketorder_";
            }else {
                prefix = "op_buytrainticketsuccessfully_";
            }
            sms.setTicketType(TicketType.TRAIN);
            sms.setSender((String) map.get(prefix + "channelname"));
            sms = Utils.getTrainSeatNoType(sms);//, map.get(prefix + "train_seat_arr_complex"));

            sms.setTitle(sms.getTrainNo() + " " + sms.getFromStation() + "站发车");
            sms.setSubTitle(map.get(prefix + "departuredate_arr") + " " + map.get(prefix + "departuretime_arr") + " 出发");
            sms.setFromDate((String) map.get(prefix + "departuredate_arr"));
            sms.setToDate((String) map.get(prefix + "arrivaldate_arr"));
            sms.setFromTime((String) map.get(prefix + "departuretime_arr"));
            sms.setToTime((String) map.get(prefix + "arrivaltime_arr"));
            sms.setButton1(ButtonType.TAXI);

            sms.setDueToDate(DateUtils.defaultParse((String) (map.get(prefix + "checkindate"))));
        }else {
            return null;
        }

        return sms;
    }

    private static String getAirlineFromSceneId(String sid){
        String res = "";
        if("05001009".equals(sid)){
            res = "深圳航空";
        }else if("05001012".equals(sid)){
            res = "华胜航空";
        }else if("05001014".equals(sid)){
            res = "海南航空";
        }else if("05001024".equals(sid)){
            res = "中国民航";
        }else if("05001025".equals(sid)){
            res = "山东航空";
        }else if("05001026".equals(sid)){
            res = "东方航空";
        }else if("05001027".equals(sid)){
            res = "中国国航";
        }else if("05001033".equals(sid)){
            res = "厦门航空";
        }else if("05001034".equals(sid)){
            res = "四川航空";
        }else if("05001035".equals(sid)){
            res = "资中蓝天航空";
        }else if("05003013".equals(sid)){
            res = "南方航空";
        }else if("05003037".equals(sid)){
            res = "春秋航空";
        }else if("05003038".equals(sid)){
            res = "华联航空";
        }else if("05003054".equals(sid)){
            res = "西部航空";
        }else{
            res = "";
        }

        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSmsCenterNum() {
        return smsCenterNum;
    }

    public void setSmsCenterNum(String smsCenterNum) {
        this.smsCenterNum = smsCenterNum;
    }

    public String getScendId() {
        return scendId;
    }

    public void setScendId(String scendId) {
        this.scendId = scendId;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public ButtonType getButton1() {
        return button1;
    }

    public void setButton1(ButtonType button1) {
        this.button1 = button1;
    }

    public ButtonType getButton2() {
        return button2;
    }

    public void setButton2(ButtonType button2) {
        this.button2 = button2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Date getDueToDate() {
        return dueToDate;
    }

    public void setDueToDate(Date dueToDate) {
        this.dueToDate = dueToDate;
    }

    public Date getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(Date usedDate) {
        this.usedDate = usedDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(String roomAmount) {
        this.roomAmount = roomAmount;
    }

    public String getNightAmount() {
        return nightAmount;
    }

    public void setNightAmount(String nightAmount) {
        this.nightAmount = nightAmount;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSeeAdd() {
        return seeAdd;
    }

    public void setSeeAdd(String seeAdd) {
        this.seeAdd = seeAdd;
    }

    public String getTicketAdd() {
        return ticketAdd;
    }

    public void setTicketAdd(String ticketAdd) {
        this.ticketAdd = ticketAdd;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
}
