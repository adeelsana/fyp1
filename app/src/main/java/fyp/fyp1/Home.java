package fyp.fyp1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    ArrayList<Item> itemArrayList = new ArrayList<>();
    int year,month,day;
    RecyclerView listview;
    private FirebaseAuth mAuth;
    int quantity = 0;
    LinearLayout rootLayout;
    //    Item item;
    Location location;
    LatLng destinationLocation;
    TextView navEmail,navName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        navEmail=(TextView)findViewById(R.id.navEmail);
//        navEmail.setText("Jacksparrow@gmail.com");
        FirebaseUser currentFirebaseUser = mAuth.getInstance().getCurrentUser() ;

        location = new Location();
//        item = new Item();
        rootLayout = (LinearLayout) findViewById(R.id.addItemRootLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                showAddItemDialogue();
            }
        });
        FloatingActionButton fabLocation = (FloatingActionButton) findViewById(R.id.location);
        fabLocation.setImageResource(R.drawable.ic_place);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder1 = new PlacePicker.IntentBuilder();
                try {
                    Intent i = builder1.build(Home.this);
                    startActivityForResult(i, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
//        name = (TextView)header.findViewById(R.id.username);
        navEmail = (TextView)header.findViewById(R.id.navEmail);
        navName = (TextView)header.findViewById(R.id.navName);

//        name.setText(personName);
        navEmail.setText((CharSequence) currentFirebaseUser.getEmail());

        showRiderConfirmationDialogue();
        int nameSize=navEmail.getText().toString().length();
        String name=navEmail.getText().toString().substring(0,nameSize-10);
        navName.setText(name);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                destinationLocation = place.getLatLng();
                location.setLat(String.valueOf(destinationLocation.latitude));
                location.setLng(String.valueOf(destinationLocation.longitude));
                location.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                location.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                location.setStatus(0);
                location.setItemArrayList(itemArrayList);
                showConfirmationDialogue();

            }
        }
    }

    private void showRiderConfirmationDialogue() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Locations");
        //adding a value event listener so if data in database changes it does in textview also not needed at the minute
//        final Location usersData=new Location();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(Home.this, "YEAAA ", Toast.LENGTH_SHORT).show();
                    Location Data = postSnapshot.getValue(Location.class);
                    Toast.makeText(Home.this, Data.toString(), Toast.LENGTH_SHORT).show();

                    if(Data.getStatus()==1){
                        final AlertDialog.Builder dialog=new AlertDialog.Builder(Home.this);
                        dialog.setTitle("Confirmed");
                        dialog.setMessage("Finalized");

                        LayoutInflater inflater =LayoutInflater.from(Home.this);
                        View reset_layout=inflater.inflate(R.layout.confirmation,null);

                        dialog.setView(reset_layout);
                        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startService(new Intent(Home.this, Myservice.class));

                            }

                        });

                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
    }

    private void showConfirmationDialogue() {

        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Confirmation");
        dialog.setMessage(" Are you Ready to Upload The Need");

        LayoutInflater inflater =LayoutInflater.from(this);
        View reset_layout=inflater.inflate(R.layout.confirmation,null);

//
        dialog.setView(reset_layout);
//        SpotsDialog waitingDialog;
        dialog.setPositiveButton("Urgent", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DatabaseReference locations = FirebaseDatabase.getInstance().getReference("Locations");
                locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(location)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                location.setItemArrayList(itemArrayList);
                                Snackbar.make(findViewById(android.R.id.content),"Uploaded Successfully",Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(android.R.id.content),"Upload Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });

                startService(new Intent(Home.this, Myservice.class));

            }



        });

        dialog.setNegativeButton("Normal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                addPendingOrders();

                //stopService(new Intent(MainActivity.this, Myservice.class));
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Home.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
        dialog.show();

    }
    private void addPendingOrders(){

        DatabaseReference pendingOrders = FirebaseDatabase.getInstance().getReference("PendingRequests");
        pendingOrders.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        location.setItemArrayList(itemArrayList);
                        Snackbar.make(findViewById(android.R.id.content),"Uploaded pending Need Successfully",Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content),"Upload pending Need Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void showAddItemDialogue() {

        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("    UPLOAD NEED  ");
        dialog.setMessage("Please Add Information ");

        LayoutInflater inflater =LayoutInflater.from(this);
        View reset_layout=inflater.inflate(R.layout.add_item,null);

//        final MaterialEditText edtEmail=reset_layout.findViewById(R.id.edtEmail);
        final MaterialEditText name=reset_layout.findViewById(R.id.Name);
        final MaterialEditText quantity=reset_layout.findViewById(R.id.chooseQuantity);
        final MaterialEditText contactno=reset_layout.findViewById(R.id.contactno);
        final MaterialEditText description=reset_layout.findViewById(R.id.descrip);

        dialog.setView(reset_layout);
//        SpotsDialog waitingDialog;
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                    Item item = new Item();
                if(TextUtils.isEmpty(name.getText().toString())) {

                    Snackbar.make(findViewById(android.R.id.content),"Please enter  Name", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(name.getText().toString())){
                    item.setName(name.getText().toString());
                }
                if(TextUtils.isEmpty(quantity.getText().toString())) {

                    Snackbar.make(findViewById(android.R.id.content),"Please enter Quantity", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(quantity.getText().toString())){
                    item.setQuantity(quantity.getText().toString());
                }

                if(TextUtils.isEmpty(contactno.getText().toString())) {
                  Snackbar.make(findViewById(android.R.id.content),"Please enter contact number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(contactno.getText().toString())){
                    item.setContactno(contactno.getText().toString());
                }
                    item.setDescription(description.getText().toString());



//                waitingDialog=new SpotsDialog(Home.this);
//                waitingDialog.show();
                Log.d("item",item.toString());

                itemArrayList.add(item);
                //Recycler View
                listview=(RecyclerView) findViewById(R.id.listView);
                addItemAdapter addItemAdapter=new addItemAdapter(itemArrayList);
                listview.setLayoutManager(new LinearLayoutManager(Home.this));
                addItemAdapter.notifyDataSetChanged();
                listview.setAdapter(addItemAdapter);
            }



        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //Pending Orders
//            startActivity(new Intent(Home.this,PendingOrders.class));


        } else if (id == R.id.nav_manage) {

        } else if(id==R.id.sign_out){
            //sign out
            mAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(Home.this,MainActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        year =  year;
        month =  monthOfYear;
        day = dayOfMonth;
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                Home.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true

        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Date date = new Date(year + 118,month,day,hourOfDay,minute,second);

        System.out.print(date);
        Log.v("date",date.toString());
        // creating timer task, timer
        TimerTask tasknew = new TimerTask() {
            @Override
            public void run() {
                Log.v("sech","00000");


            }
        };
        Timer timer = new Timer();

        // scheduling the task at interval
        Long sec  = date.getTime()/1000;

        Log.v("sec",sec.toString());


        timer.schedule(tasknew, date,100000000);
    }
    // this method performs the task

}
