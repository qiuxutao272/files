package cn.yunzhisheng.vui.assistant.tv.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.util.ImageDownloader;
import cn.yunzhisheng.vui.modes.StockInfo;

public class StockContentView extends FrameLayout implements ISessionView {
	private static final String TAG = "StockContentView";

	private TextView mTextViewName, mTextViewCode;
	private TextView mTextViewCurrentPrice, mTextViewChangeAmount,
			mTextViewChangeRate, mTextViewHighestPrice, mTextViewLowestPrice,
			mTextViewYesterdayClosingPrice, mTextViewTodayOpeningPrice;
	private ImageView mImgTrend, mImgChart;

	private final int mColorUp, mColorDown;
	private ImageDownloader mImageDownloader;

	public StockContentView(Context context) {
		super(context);
		mImageDownloader = new ImageDownloader(
				PrivatePreference.FOLDER_PACKAGE_CACHE
						+ PrivatePreference.FOLDER_IMG, 0);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(Res.layout.stock_content_view, this, true);
		setFocusable(true);
		findViews();
		Resources res = getResources();
		mColorUp = res.getColor(Res.color.text_stock_up);
		mColorDown = res.getColor(Res.color.text_stock_down);
	}

	private void findViews() {
		mTextViewName = (TextView) findViewById(Res.id.textViewStockName);
		mTextViewCode = (TextView) findViewById(Res.id.textViewStockCode);
		mTextViewCurrentPrice = (TextView) findViewById(Res.id.textViewStockCurrentPrice);
		mTextViewChangeAmount = (TextView) findViewById(Res.id.textViewStockChangeAmount);
		mTextViewChangeRate = (TextView) findViewById(Res.id.textViewStockChangeRate);
		mTextViewHighestPrice = (TextView) findViewById(Res.id.textViewStockHighestPrice);
		mTextViewLowestPrice = (TextView) findViewById(Res.id.textViewStockLowestPrice);
		mTextViewYesterdayClosingPrice = (TextView) findViewById(Res.id.textViewStockYesterdayClosingPrice);
		mTextViewTodayOpeningPrice = (TextView) findViewById(Res.id.textViewStockTodayOpeningPrice);
		mImgTrend = (ImageView) findViewById(Res.id.imgViewStockTrend);
		mImgChart = (ImageView) findViewById(Res.id.imgViewStockChart);
		mImgTrend.setVisibility(View.GONE);
	}

	@Override
	public boolean isTemporary() {
		return false;
	}

	public void setStockInfo(StockInfo info) {
		mTextViewName.setText(info.getName());
		mTextViewCode.setText(info.getCode());

		mTextViewCurrentPrice.setText(info.getCurrentPrice());
		mTextViewChangeAmount.setText(String.valueOf(info.getChangeAmount()));
		mTextViewChangeRate.setText(info.getChangeRate());

		mTextViewTodayOpeningPrice
				.setText("今开价:" + info.getTodayOpeningPrice());
		mTextViewYesterdayClosingPrice.setText("昨收价:"
				+ info.getYesterdayClosingPrice());
		mTextViewHighestPrice.setText("最高价:" + info.getHighestPrice());
		mTextViewLowestPrice.setText("最低价:" + info.getLowestPrice());
		mImgTrend.setVisibility(View.VISIBLE);
		if (info.getChangeAmount() > 0) {
			mTextViewCurrentPrice.setTextColor(mColorUp);
			mImgTrend.setImageResource(Res.drawable.ic_stock_trend_up);
			mTextViewChangeAmount.setTextColor(mColorUp);
			mTextViewChangeRate.setTextColor(mColorUp);
		} else {
			mTextViewCurrentPrice.setTextColor(mColorDown);
			mImgTrend.setImageResource(Res.drawable.ic_stock_trend_down);
			mTextViewChangeAmount.setTextColor(mColorDown);
			mTextViewChangeRate.setTextColor(mColorDown);
		}

		mImageDownloader.download(info.getChartImgUrl(), mImgChart, true, ImageDownloader.SCALE_BY_IMAGEVIEW_WIDTH);
	}

	@Override
	public void requestSuperFocus() {

	}

	/**
	 * @Description	: release
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-12-19
	 * @see cn.yunzhisheng.vui.assistant.tv.view.ISessionView#release()
	 */
	@Override
	public void release() {
		
	}

}
