package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.chatapplication.Fragments.CallsFragment;
import com.example.chatapplication.Fragments.ChatFragment;
import com.example.chatapplication.Fragments.ContactsFragment;
import com.example.chatapplication.Model.Chat;
import com.example.chatapplication.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    CircleImageView profileImage, main_unrReadMsgCircular;
    TextView main_unrReadMsgCount, main_unrReadMsgText;

    CurvedBottomNavigationView bottomNavigationView;
    VectorMasterView fab1, fab2, fab3;
    RelativeLayout lin_id;
    PathModel outline;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profileImage = findViewById(R.id.main_profileImage);
        main_unrReadMsgCircular = findViewById(R.id.main_unrReadMsgCircular);
        main_unrReadMsgCount = findViewById(R.id.main_unrReadMsgCount);
        main_unrReadMsgText = findViewById(R.id.main_unrReadMsgText);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        fab1 = findViewById(R.id.main_fab);
        fab2 = findViewById(R.id.main_fab1);
        fab3 = findViewById(R.id.main_fab2);

        lin_id = findViewById(R.id.main_lin_id);
        bottomNavigationView.inflateMenu(R.menu.nav_menu);

        //Set event for bottom nav
        bottomNave();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid());

        reference();
        viewProfile();
        unReadMsgMethod();

    }

    private void reference(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageURL().equals("default"))
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                else
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unreadMSG = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isSeen()){
                        unreadMSG++;
                    }
                }
                if (unreadMSG == 0){
                    main_unrReadMsgCircular.setVisibility(View.GONE);
                    main_unrReadMsgCount.setVisibility(View.GONE);
                    main_unrReadMsgText.setVisibility(View.GONE);
                } else {
                    main_unrReadMsgCircular.setVisibility(View.VISIBLE);
                    main_unrReadMsgCount.setText("0" +String.valueOf(unreadMSG));
                    if (unreadMSG == 1){
                        main_unrReadMsgText.setText("CHAT");
                    } else {
                        main_unrReadMsgText.setText("CHATS");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void unReadMsgMethod(){
        main_unrReadMsgCircular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = new ChatFragment();

                //animation
                draw(6);
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab1);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            }
        });

        main_unrReadMsgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = new ChatFragment();

                //animation
                draw(6);
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab1);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        });

    }

    private void viewProfile(){
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    class ViewAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;


        public ViewAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment){
            fragments.add(fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    private void bottomNave() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.chat:
                selectedFragment = new ChatFragment();

                //animation
                draw(6);
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab1);
                break;

            case R.id.contacts:
                selectedFragment = new ContactsFragment();

                //animation
                draw(2);
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab2);
                break;

            case R.id.call:
                selectedFragment = new CallsFragment();

                //animation
                draw();
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.VISIBLE);
                drawAnimation(fab3);
                break;

            default:
                selectedFragment = new ChatFragment();

                //animation
                draw(6);
                //FInd the correct path using name
                lin_id.setX(bottomNavigationView.firstCurveControlPoint.x);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                fab3.setVisibility(View.GONE);
                drawAnimation(fab1);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    }

    private void draw() {
        bottomNavigationView.firstCurveStartPoint.set((bottomNavigationView.navigationBarWidth* 10/12)
        -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
        -(bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.firstCurveEndPoint.set(bottomNavigationView.navigationBarWidth* 10/12,
                bottomNavigationView.CURVE_CIRCLE_RADIUS
                        +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.secondCurveStartPoint = bottomNavigationView.firstCurveEndPoint;
        bottomNavigationView.secondCurveEndPoint.set((bottomNavigationView.navigationBarWidth * 10/12)
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+(bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.firstCurveControlPoint.set(bottomNavigationView.firstCurveStartPoint.x
                        + bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.firstCurveStartPoint.y);

        bottomNavigationView.firstCurveControlPoint2.set(bottomNavigationView.firstCurveEndPoint.x
                        -(bottomNavigationView.CURVE_CIRCLE_RADIUS/2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.firstCurveEndPoint.y);

        //Sme for the second Curve
        bottomNavigationView.secondCurveControlPoint.set(bottomNavigationView.secondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.secondCurveStartPoint.y);

        bottomNavigationView.secondCurveControlPoint2.set(bottomNavigationView.secondCurveEndPoint.x
                        -(bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.secondCurveEndPoint.y);

    }

    public void draw(int i) {
        bottomNavigationView.firstCurveStartPoint.set((bottomNavigationView.navigationBarWidth/i)
                -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) -(bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.firstCurveEndPoint.set(bottomNavigationView.navigationBarWidth/i,
                bottomNavigationView.CURVE_CIRCLE_RADIUS +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.secondCurveStartPoint = bottomNavigationView.firstCurveEndPoint;
        bottomNavigationView.secondCurveEndPoint.set((bottomNavigationView.navigationBarWidth/i)
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)+(bottomNavigationView.CURVE_CIRCLE_RADIUS/3), 0);

        bottomNavigationView.firstCurveControlPoint.set(bottomNavigationView.firstCurveStartPoint.x
        + bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.firstCurveStartPoint.y);

        bottomNavigationView.firstCurveControlPoint2.set(bottomNavigationView.firstCurveEndPoint.x
                        -(bottomNavigationView.CURVE_CIRCLE_RADIUS/2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.firstCurveEndPoint.y);

        //Sme for the second Curve
        bottomNavigationView.secondCurveControlPoint.set(bottomNavigationView.secondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.secondCurveStartPoint.y);

        bottomNavigationView.secondCurveControlPoint2.set(bottomNavigationView.secondCurveEndPoint.x
                        -(bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.secondCurveEndPoint.y);
    }

    public void drawAnimation(final VectorMasterView fab1) {
        outline = fab1.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                outline.setTrimPathEnd((Float)valueAnimator.getAnimatedValue());
                fab1.update();
            }
        });
        valueAnimator.start();
    }

    private void isOnline(String online){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("isOnline", online);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnline("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnline("offline");
    }
}
