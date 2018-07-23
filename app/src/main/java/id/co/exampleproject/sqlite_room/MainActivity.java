package id.co.exampleproject.sqlite_room;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import id.co.exampleproject.sqlite_room.adapter.AdapterCatatan;
import id.co.exampleproject.sqlite_room.db.AppDatabase;
import id.co.exampleproject.sqlite_room.db.entity.Catatan;

public class MainActivity extends AppCompatActivity {
    private Catatan catatan;
    private AppDatabase database;
    private RecyclerView recyclerView;
    private AdapterCatatan adapterCatatan;
    private LinearLayout linearEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipe);
        linearEmpty = findViewById(R.id.linearempty);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        catatan = new Catatan();

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "catatandb").build();

        getCatatan();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCatatan();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                insertCatatan(view);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void getCatatan() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final List<Catatan> catatans = database.getCatatanDao().getAll();

                if (catatans.size() <= 0) {
                    swipeRefreshLayout.setVisibility(View.GONE);
                    linearEmpty.setVisibility(View.VISIBLE);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            linearEmpty.setVisibility(View.GONE);
                            adapterCatatan = new AdapterCatatan(catatans, new AdapterCatatan.OnItemClickListener() {
                                @Override
                                public void onClick(final Catatan catatan) {
                                    View viewDialog = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_create_catatan, null);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle(R.string.tambah_catatan);
                                    builder.setView(viewDialog);

                                    final EditText note = viewDialog.findViewById(R.id.etCatatan);
                                    note.setText(catatan.catatan);

                                    builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            catatan.catatan = note.getText().toString();

                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    database.getCatatanDao().update(catatan);

                                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                    return null;
                                                }
                                            }.execute();
                                        }
                                    });

                                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    database.getCatatanDao().delete(catatan);

                                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                    return null;
                                                }
                                            }.execute();
                                        }
                                    });

                                    builder.show();
                                }
                            });
                            recyclerView.setAdapter(adapterCatatan);
                        }
                    });
                }

                return null;
            }
        }.execute();
    }

    private void insertCatatan(final View view) {
        View viewDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_create_catatan, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.tambah_catatan);
        builder.setView(viewDialog);

        SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());

        final EditText note = viewDialog.findViewById(R.id.etCatatan);
        final String date = dateF.format(Calendar.getInstance().getTime());
        final String time = timeF.format(Calendar.getInstance().getTime());

        builder.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tanggal = date + " " + time;
                String etcatatan = note.getText().toString();

                if (etcatatan.equals("")) {
                    Snackbar.make(view, R.string.catatan_kosong, Snackbar.LENGTH_LONG).show();
                } else {
                    catatan.tanggal = tanggal;
                    catatan.catatan = etcatatan;

                    new AsyncTask<Void, Void, Long>() {
                        @Override
                        protected Long doInBackground(Void... voids) {
                            database.getCatatanDao().create(catatan);

                            return null;
                        }
                    }.execute();

                    Snackbar.make(view, R.string.catatan_simpan, Snackbar.LENGTH_LONG).show();

                    getCatatan();
                }
            }
        });

        builder.setNegativeButton("Gagal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
