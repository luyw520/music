package com.music;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.Mp3Info;
import com.music.lu.R;
import com.music.lu.utils.DialogUtil;
import com.music.lu.utils.FileUtils;
import com.music.lu.utils.LogUtil;
import com.music.lu.utils.MediaUtil;
import com.music.lu.utils.Mp3FileHelper;
import com.music.lu.utils.Mp3Util;
import com.music.mp3.FileHelper;
import com.music.widget.dialog.LoadingView;

@ContentView(value = R.layout.activity_searchmusic)
public class SearchMusicActivity extends BaseActivity {

	private static final String TAG = "SearchMusicActivity";
	@ViewInject(value = R.id.tv_title)
	TextView tv_title;
	@ViewInject(value = R.id.tv_no_song)
	TextView tv_no_song;
	@ViewInject(value = R.id.tv_singer)
	TextView tv_singer;
	@ViewInject(value = R.id.iv_search)
	ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	ImageView iv_more;
	@ViewInject(value = R.id.iv_searchMusic)
	ImageView iv_searchMusic;

	@ViewInject(value = R.id.auto_tv_key)
	AutoCompleteTextView auto_tv_key;
	@ViewInject (value = R.id.listView)
	ListView listView;
	
	@ViewInject(value=R.id.loadView)
	private LoadingView loadView;
	
	private Mp3Info mp3=new Mp3Info();
	private List<Mp3Info> datas=new ArrayList<Mp3Info>();
	
	private MyAdapter myAdapter=new MyAdapter();
	private String songName,songer;
	private String path="";
	HttpUtils httpUtils;
	String encode="";
	String decode="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initWidget();
	}

	@OnClick({ R.id.iv_back, R.id.iv_searchMusic})
	public void onClickListener(View v) {

		LogUtil.i(this.getClass(), "click");
		switch (v.getId()) {
		case R.id.iv_searchMusic:
			loadView.setLoadingText("音乐搜索中..");
			loadView.setVisibility(View.VISIBLE);
			
			handler.sendEmptyMessageDelayed(0, 3000);
			break;
		case R.id.iv_back:
			finish();
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			searchMusic();
		};
	};
	private void searchMusic() {
		songName=auto_tv_key.getText().toString();
		songer=tv_singer.getText().toString();
		String path="http://box.zhangmen.baidu.com/x?op=12&count=1&title="+songName+"$$"+songer+"$$$$";
		httpUtils=new HttpUtils();
//		DialogUtil.showWaitDialog(SearchMusicActivity.this, "音乐搜索", "正在搜索中...");
//		DialogUtil.showCustomWaitDialog(this);
		
		httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
//				DialogUtil.closeAlertDialog();
				loadView.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0.result);
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				String result=arg0.result;
				try {
					SAXReader saxReader=new SAXReader();
					
					Document document=saxReader.read(new ByteArrayInputStream(result.getBytes()));
					Element element=document.getRootElement();
					encode="";
					decode="";
					datas.clear();
					listNodes(element);
					
//					if(datas.size()>0){
//						tv_no_song.setVisibility(View.GONE);
//						listView.setVisibility(View.VISIBLE);
//						myAdapter.notifyDataSetChanged();
//					}else{
//						tv_no_song.setVisibility(View.VISIBLE);
//						listView.setVisibility(View.GONE);
//					}
					
					
					if(!encode.equals("")&&!decode.equals("")){
						String url=encode.substring(0, encode.lastIndexOf("/")+1)+decode;
						System.out.println("down url:"+url);
						mp3.setDownUrl(url);
						mp3.setArtist(songer);
						mp3.setTitle(songName);
						mp3.setAlbum(songer);
						mp3.setUrl(url);
						mp3.setTitlepinyin(MediaUtil.toHanyuPinYin(songName));
						mp3.setDisplayName(songer+"-"+songName+".mp3");
						datas.add(mp3);
						tv_no_song.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
						myAdapter.notifyDataSetChanged();
					}else{
						tv_no_song.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					tv_no_song.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}finally{
					
//					DialogUtil.closeAlertDialog();
					loadView.setVisibility(View.GONE);
				}
			}
		});
	}
	/**
	 * 获取节点名称和内容
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	public  void listNodes(Element node) {  
		Mp3Info mp3Info=new Mp3Info();
        System.out.println("当前节点的名称：：" + node.getName());  
        // 获取当前节点的所有属性节点  
		List<Attribute> list = node.attributes();  
        // 遍历属性节点  
        for (Attribute attr : list) {  
            System.out.println(attr.getText() + "-----" + attr.getName()  
                    + "---" + attr.getValue());  
        }  
        if(node.getName().equals("type")){
//        	mp3.setTitle(auto_tv_key.getText().toString()+"."+node.getText());
        }
        if(node.getName().equals("size")){
        	mp3Info.setSize(Long.parseLong(node.getText()));
        }
        if(node.getName().equals("decode")){
        	
        	decode=node.getText();;
        }
        if(node.getName().equals("encode")){
        	
        	encode=node.getText();
        }
        if (!(node.getTextTrim().equals(""))) {  
            System.out.println("文本内容：：：：" + node.getText());  
        }  
//    	if(!encode.equals("")&&!decode.equals("")){
//			String url=encode.substring(0, encode.lastIndexOf("/")+1)+decode;
//			System.out.println("down url:"+url);
//			mp3Info.setDownUrl(url);
//			
//			Mp3FileHelper.setMp3Info(url,mp3Info);
//			datas.add(mp3Info);
//    	}
    	
    	
        // 当前节点下面子节点迭代器  
        Iterator<Element> it = node.elementIterator();  
        // 遍历  
        while (it.hasNext()) {  
            // 获取某个子节点对象  
            Element e = it.next();  
            // 对子节点进行遍历  
            listNodes(e);  
        }  
    }  
	private void initWidget() {
		tv_title.setText("搜索音乐");
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		listView.setAdapter(myAdapter);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			
//			if(datas)
			ViewHolder viewHolder=null;
			if(convertView==null){	
				
				convertView=LayoutInflater.from(SearchMusicActivity.this).inflate(R.layout.item_search_music, null);
				viewHolder=new ViewHolder();
				viewHolder.tv_songName=(TextView)convertView.findViewById(R.id.tv_songName);
				viewHolder.btn_down=(Button) convertView.findViewById(R.id.btn_down);
				viewHolder.tv_downing=(TextView) convertView.findViewById(R.id.tv_down);
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder) convertView.getTag();
				
			}
			viewHolder.tv_songName.setText(datas.get(position).getDisplayName());
			viewHolder.btn_down.setOnClickListener(new DownOnClick(viewHolder.tv_downing,position));
			return convertView;
		}
		class ViewHolder {
			TextView tv_songName;
			Button btn_down;
			TextView tv_downing;
		} 
		class DownOnClick implements OnClickListener{
			private TextView textView;
			private int position;
			public DownOnClick(TextView textView,int position){
				this.textView=textView;
				this.position=position;
			}
			@Override
			public void onClick(View v) {
				final Button button=(Button) v;
				String flag=button.getText().toString();
				
				if("下载".equals(flag)){
					down(button);
				}else if("点击播放".equals(flag)){
					
					play();
					down(button);
				}
				
				
				
			}
			private void down(final Button button){
//				textView.setVisibility(View.VISIBLE);
//				button.setText("下载中..");
				HttpUtils http=new HttpUtils();
				
//				button.setEnabled(false);
				
				
//				path=FileUtils.downPath()+File.separator+datas.get(position).getTitle();
				path=findLocalMp3(datas.get(position).getDisplayName());
				if(path!=null){
//					mp3.setUrl(path);
					return;
				}
				path=FileUtils.downPath()+File.separator+datas.get(position).getDisplayName();
				http.download(datas.get(position).getDownUrl(), path, true, new RequestCallBack<File>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
						
//						float c=current;
//						float t=total;
//						float a=c/t*100;
//						System.out.println("a:"+a);
//						textView.setText((int)a+"%");
//						System.out.println("total:"+total);
//						System.out.println("current:"+current);
//						System.out.println((int)((current/total)*100)+"%");
					}
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub
//						button.setEnabled(true);
//						button.setText("点击播放");
//						MP3File file;
//						try {
//							file = new MP3File(path);
//							AbstractID3v2 id3v2 = file.getID3v2Tag();
//							
//							if (id3v2 != null) {
//								mp3.setDuration(id3v2.getSize());
//							}
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (TagException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}// 1,lyrics
//						
//						mp3.setUrl(path);
						DialogUtil.showToast(getApplicationContext(), mp3.getTitle()+"下载成功");
					}
					
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						System.out.println("下载出错");
						button.setEnabled(true);
						button.setText("下载");
						DialogUtil.showToast(SearchMusicActivity.this, "下载出错");
						textView.setVisibility(View.INVISIBLE);
					}
				});
			}
		}
		
	}
	private String findLocalMp3(String mp3Path){
		
		
		File file=new File(FileUtils.downPath());
		for(File f:file.listFiles()){
			
			if(f.getParent().equals(mp3Path)){
				Log.i(TAG, "本地歌曲存在...");
				return f.getAbsolutePath();
			}
		}
		return null;
	}
	private void play(){
//		EventBus.getDefault().post(mp3, TagUtil.TAG_ACTION_PLAY_BY_PATH);
		Mp3Util.getDefault().playMusic(mp3);
//		Mp3Util.getDefault().getMp3Infos().add(mp3);
		startActivity(new Intent(this,PlayerActivity.class));
	}
}
