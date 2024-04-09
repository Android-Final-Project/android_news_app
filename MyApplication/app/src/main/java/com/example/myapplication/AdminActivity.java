package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.myapplication.customadapter.SourceAdapter;
import com.example.myapplication.customadapter.SourceSpinnerAdapter;
import com.example.myapplication.model.BanedSources;
import com.example.myapplication.model.Comment;
import com.example.myapplication.model.Language;
import com.example.myapplication.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Source;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.response.SourcesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity {

    List<Source> sources = new ArrayList<>();
    List<BanedSources> banedSources = new ArrayList<>();
    List<String> sourceSpinnerItems = new ArrayList<>();

    Spinner sourceSpinner;

    ListView sorceListView;

    private SourceAdapter sourceAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupBottomNavigationView();
        sourceSpinner = findViewById(R.id.spinnerDomain);
        sorceListView = findViewById(R.id.domainListView);

        sourceAdapter = new SourceAdapter(AdminActivity.this, banedSources);
        sorceListView.setAdapter(sourceAdapter);

        getSourcesFromApi();
        getBannedSources();
        sourceSpinner.setOnItemSelectedListener(onItemChangeListener());
    }

    private void getBannedSources() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference(BanedSources.DB_REFERENCE);
        Query bannedSourcesQuery = reference.orderByChild("id");
        bannedSourcesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sourceAdapter.getItems().clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    sourceAdapter.getItems().add(postSnapshot.getValue(BanedSources.class));
                }
                sourceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @NonNull
    private AdapterView.OnItemSelectedListener onItemChangeListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sourceId = (String) parent.getSelectedItem();

                if(!sourceId.isEmpty()){
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference reference = db.getReference(BanedSources.DB_REFERENCE);
                    Query checkSourceId = reference.orderByChild("id").equalTo(sourceId);
                    checkSourceId.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                reference.child(sourceId).setValue(new BanedSources(sourceId), (error, ref) -> {
                                    if (Objects.nonNull(error)) {
                                        Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AdminActivity.this, "Source removed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }


    void getSourcesFromApi() {
        NewsApiClient newsApiClient = new NewsApiClient("997b299131dc4beb8edfceba3e9ad34a");
        newsApiClient.getSources(
                new SourcesRequest.Builder().language("en").country("us").build(),
                new NewsApiClient.SourcesCallback() {
                    @Override
                    public void onSuccess(SourcesResponse response) {
                        runOnUiThread(() -> {
                            sourceSpinnerItems.clear();
                            sourceSpinnerItems.add("");
                            sourceSpinnerItems.addAll(response.getSources().stream().map(Source::getId).collect(Collectors.toList()));
                            Log.d("AdminActicity", "Sources fetched: " + sources.size());
                            sourceSpinner.setAdapter(new SourceSpinnerAdapter(AdminActivity.this, sourceSpinnerItems));
                        });
                    }


                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(AdminActivity.this, "Failed to fetch sources", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.search_view) {
                intent = new Intent(AdminActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_saved) {
                intent = new Intent(AdminActivity.this, SavedActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                intent = new Intent(AdminActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}