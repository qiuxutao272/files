/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.yunzhisheng.vui.assistant.tv.media;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

public class PlayList implements Serializable {
	private static final String TAG = "PlayList";

	private static final long serialVersionUID = 1L;

	public enum PlaylistPlaybackMode {
		NORMAL, SHUFFLE, REPEAT, SHUFFLE_AND_REPEAT
	}

	protected ArrayList<Track> playlist = null;

	protected int selected = -1;

	private ArrayList<Integer> mPlayOrder = new ArrayList<Integer>();

	private PlaylistPlaybackMode mPlaylistPlaybackMode = PlaylistPlaybackMode.NORMAL;

	public PlayList() {
		playlist = new ArrayList<Track>();

		calculateOrder(true);
	}

	public PlaylistPlaybackMode getPlaylistPlaybackMode() {
		return mPlaylistPlaybackMode;
	}

	public void setPlaylistPlaybackMode(PlaylistPlaybackMode aPlaylistPlaybackMode) {
		boolean force = false;
		switch (aPlaylistPlaybackMode) {
		case NORMAL:
		case REPEAT:
			if (mPlaylistPlaybackMode == PlaylistPlaybackMode.SHUFFLE || mPlaylistPlaybackMode == PlaylistPlaybackMode.SHUFFLE_AND_REPEAT) {
				force = true;
			}
			break;
		case SHUFFLE:
		case SHUFFLE_AND_REPEAT:
			if (mPlaylistPlaybackMode == PlaylistPlaybackMode.NORMAL || mPlaylistPlaybackMode == PlaylistPlaybackMode.REPEAT) {
				force = true;
			}
			break;
		}
		mPlaylistPlaybackMode = aPlaylistPlaybackMode;
		calculateOrder(force);
	}

	public void addTrack(Track track) {
		playlist.add(track);
		mPlayOrder.add(size() - 1);
	}

	public Track findTrack(String title, String artist) {
		for (Track track : playlist) {
			if (track.getTitle().equals(title) && track.getArtist().equals(artist)) {
				return track;
			}
		}

		return null;
	}

	public boolean isEmpty() {
		return playlist.size() == 0;
	}

	public void selectNext() {
		if (!isEmpty()) {
			selected++;
			selected %= playlist.size();
			if (Log.isLoggable(TAG, Log.DEBUG)) {
				Log.d("TAG", "Current (next) selected = " + selected);
			}
		}
	}

	public void selectPrev() {
		if (!isEmpty()) {
			selected--;
			if (selected < 0) selected = playlist.size() - 1;
		}
		if (Log.isLoggable(TAG, Log.DEBUG)) {
			Log.d("TAG", "Current (prev) selected = " + selected);
		}
	}

	public void select(int index) {
		if (!isEmpty()) {
			if (index != selected) {
				if (index >= 0 && index < playlist.size()) {
					selected = mPlayOrder.indexOf(index);
				}
			}
		}
	}

	public void selectOrAdd(Track track) {

		// first search thru available tracks
		for (int i = 0; i < playlist.size(); i++) {
			if (playlist.get(i).getTitle().equals(track.getTitle())) {
				select(i);
				return;
			}
		}

		// add track if necessary
		addTrack(track);
		select(playlist.size() - 1);
	}

	public int getSelectedIndex() {
		if (isEmpty()) {
			selected = -1;
		}
		if (selected == -1 && !isEmpty()) {
			selected = 0;
		}
		return selected;
	}

	public Track getSelectedTrack() {
		Track track = null;

		int index = getSelectedIndex();
		if (index == -1) {
			return null;
		}
		index = mPlayOrder.get(index);
		if (index == -1) {
			return null;
		}
		track = playlist.get(index);

		return track;

	}

	public int size() {
		return playlist == null ? 0 : playlist.size();
	}

	public Track getTrack(int index) {
		return playlist.get(index);
	}

	public List<Track> getAllTracks() {
		return playlist;
	}

	public void remove(int position) {
		if (playlist != null && position < playlist.size() && position >= 0) {

			if (selected >= position) {
				selected--;
			}

			playlist.remove(position);
			mPlayOrder.remove(position);
		}
	}

	private void calculateOrder(boolean force) {
		if (mPlayOrder.isEmpty() || force) {
			int oldSelected = 0;

			if (!mPlayOrder.isEmpty()) {
				oldSelected = mPlayOrder.get(selected);
				mPlayOrder.clear();
			}

			for (int i = 0; i < size(); i++) {
				mPlayOrder.add(i, i);
			}

			if (mPlaylistPlaybackMode == null) {
				mPlaylistPlaybackMode = PlaylistPlaybackMode.NORMAL;
			}

			if (Log.isLoggable(TAG, Log.DEBUG)) {
				Log.d(TAG, "Playlist has been maped in " + mPlaylistPlaybackMode + " mode.");
			}

			switch (mPlaylistPlaybackMode) {
			case NORMAL:
			case REPEAT:
				selected = oldSelected;
				break;
			case SHUFFLE:
			case SHUFFLE_AND_REPEAT:
				Collections.shuffle(mPlayOrder);
				selected = mPlayOrder.indexOf(selected);
				break;
			}
		}
	}

	public boolean isLastTrackOnList() {
		if (selected == size() - 1) return true;
		else return false;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (mPlayOrder == null) {
			if (Log.isLoggable(TAG, Log.DEBUG)) {
				Log.d(TAG, "mPlayOrder is NULL");
			}
			mPlayOrder = new ArrayList<Integer>();
			calculateOrder(true);
		}
	}
}
