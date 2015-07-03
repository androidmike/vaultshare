package com.vaultshare.play.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vaultshare.play.App;
import com.vaultshare.play.R;
import com.vaultshare.play.model.Track;

import java.util.List;

/**
 * Created by mchang on 6/25/15.
 */
public class MixtapeAdapter extends BaseAdapter {
    Context context;

    public MixtapeAdapter(Context context, List<Track> tracks) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.layout_mixtape, null);
        ImageView avatarRow = (ImageView) convertView.findViewById(R.id.avatar_row);
        ImageView cover = (ImageView) convertView.findViewById(R.id.cover);
        TextView lyric1 = (TextView) convertView.findViewById(R.id.lyric_1);

        TextView lyric2 = (TextView) convertView.findViewById(R.id.lyric_2);
        TextView note = (TextView) convertView.findViewById(R.id.note);

        TextView lyric3 = (TextView) convertView.findViewById(R.id.lyric_3);

        TextView songTitle = (TextView) convertView.findViewById(R.id.song_title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        switch (position) {
            case 0:
                note.setText("Remember our junior prom? This was the theme song back then. We should probably invite Jonathan to this set too.");
                songTitle.setText("Song Cry");
                artist.setText("Jay-Z");
                cover.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.jay_z_suit));
                avatarRow.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.avatar_row_3_chat));
                lyric1.setText("[Produced by Just Blaze]\n" +
                        "\n" +
                        "[Hook - Jay-Z]\n" +
                        "I can't see 'em coming down my eyes\n" +
                        "So I gotta make the song cry\n" +
                        "I can't see 'em coming down my eyes\n" +
                        "So I gotta make the song cry\n" +
                        "\n" +
                        "[Verse 1]\n" +
                        "Good dude, I know you love me like cooked food\n" +
                        "Even though a nigga gotta move like a crook move\n" +
                        "We was together on the block since free lunch\n" +
                        "We should've been together having Four Seasons brunch\n" +
                        "We used to use umbrellas to face the bad weather\n" +
                        "So now we travel first class to change the forecast\n" +
                        "Never in bunches, just me and you\n" +
                        "I loved your point of view cause you held no punches\n" +
                        "Still I left you for months on end\n" +
                        "It's been months since I checked back in\n" +
                        "We're somewhere in a small town, somewhere locking a mall down\n" +
                        "Woodgrain, four and change, Armor All'd down\n" +
                        "I can understand why you want a divorce now\n" +
                        "Though I can't let you know it, pride won't let me show it\n" +
                        "Pretend to be heroic, that's just one to grow with\n" +
                        "But deep inside a nigga so sick");
                lyric2.setText("\n" +
                        "[Hook]\n" +
                        "\n" +
                        "[Verse 2]\n" +
                        "On repeat, the CD of BIG's \"Me and My Bitch\"\n" +
                        "Watching Bonnie and Clyde, pretending to be that shit\n" +
                        "Empty gun in your hand saying, \"Let me see that clip\"\n" +
                        "Shopping sprees, pull out your Visa quick\n" +
                        "A nigga had very bad credit, you helped me lease that whip\n" +
                        "You helped me get the keys to that V dot 6\n" +
                        "We was so happy poor but when we got rich\n" +
                        "That's when our signals got crossed, and we got flipped\n" +
                        "Rather mine, I don't know what made me leave that shit\n" +
                        "Made me speed that quick, let me see - that's it\n" +
                        "It was the cheese helped them bitches get amnesia quick\n" +
                        "I used to cut up they buddies, now they sayin they love me\n" +
                        "Used to tell they friends I was ugly and wouldn't touch me\n" +
                        "Then I showed up in that dubbed out buggy\n" +
                        "And then they got fussy and they don't remember that\n" +
                        "And I don't remember you");
                lyric3.setText("[Verse 3]\n" +
                        "A face of stone, was shocked on the other end of the phone\n" +
                        "Word back home is that you had a special friend\n" +
                        "So what was oh so special then\n" +
                        "You have given away without getting at me\n" +
                        "That's your fault, how many times you forgiven me\n" +
                        "How was I to know that you was plain sick of me?\n" +
                        "I know the way a nigga living was whack\n" +
                        "But you don't get a nigga back like that!\n" +
                        "Shit I'm a man with pride, you don't do shit like that\n" +
                        "You don't just pick up and leave and leave me sick like that\n" +
                        "You don't throw away what we had, just like that\n" +
                        "I was just fucking them girls, I was gon' get right back\n" +
                        "They say you can't turn a bad girl good\n" +
                        "But once a good girl's gone bad, she's gone forever\n" +
                        "I mourn forever, shit\n" +
                        "I've got to live with the fact I did you wrong forever\n" +
                        "\n" +
                        "[Hook]");
                break;
            case 1:

                note.setText("Can't forget about this one.");
                songTitle.setText("Creep");
                artist.setText("Radiohead");
                cover.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.radiohead_cover_large));
                avatarRow.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.avatars_row_love));
                lyric1.setText("When you were here before,\n" +
                        "Couldn't look you in the eye,\n" +
                        "You're just like an angel,\n" +
                        "Your skin makes me cry,\n" +
                        "You float like a feather,\n" +
                        "In a beautiful world,\n" +
                        "And I wish I was special,\n" +
                        "You're so fucking special.\n" +
                        "\n" +
                        "[Chorus:]\n" +
                        "But I'm a creep, I'm a weirdo,\n" +
                        "What the hell am I doing here?\n" +
                        "I don't belong here.");
                lyric2.setText("\n" +
                        "I don't care if it hurts,\n" +
                        "I want to have control,\n" +
                        "I want a perfect body,\n" +
                        "\n" +
                        "I want a perfect soul,\n" +
                        "I want you to notice,\n" +
                        "When I'm not around,\n" +
                        "You're so fucking special,\n" +
                        "I wish I was special.\n" +
                        "\n" +
                        "[Chorus:]\n" +
                        "But I'm a creep, I'm a weirdo,\n" +
                        "What the hell am I doing here?\n" +
                        "I don't belong here.\n" +
                        "\n" +
                        "Oh, oh\n");
                lyric3.setText("\n" +
                        "She's running out again,\n" +
                        "She's running out...\n" +
                        "She run run run run...\n" +
                        "Run...\n" +
                        "\n" +
                        "Whatever makes you happy,\n" +
                        "Whatever you want,\n" +
                        "You're so fucking special,\n" +
                        "I wish I was special...\n" +
                        "But I'm a creep, I'm a weirdo,\n" +
                        "What the hell am I doing here?\n" +
                        "I don't belong here,\n" +
                        "I don't belong here.");
                break;
            default:
                break;
        }

        return convertView;
    }
}