[1mdiff --git a/app/src/main/res/layout/activity_hero_browse.xml b/app/src/main/res/layout/activity_hero_browse.xml[m
[1mindex 5de5a0c..498a594 100644[m
[1m--- a/app/src/main/res/layout/activity_hero_browse.xml[m
[1m+++ b/app/src/main/res/layout/activity_hero_browse.xml[m
[36m@@ -5,7 +5,7 @@[m
         xmlns:app="http://schemas.android.com/apk/res-auto"[m
         android:layout_width="match_parent"[m
         android:layout_height="match_parent"[m
[31m-        tools:context=".HeroBrowseActivity">[m
[32m+[m[32m        tools:context=".HeroBrowseActivity" android:background="@color/cardview_dark_background">[m
 [m
 [m
     <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"[m
[1mdiff --git a/app/src/main/res/layout/activity_serie_browse.xml b/app/src/main/res/layout/activity_serie_browse.xml[m
[1mindex 474f4ef..723bbb8 100644[m
[1m--- a/app/src/main/res/layout/activity_serie_browse.xml[m
[1m+++ b/app/src/main/res/layout/activity_serie_browse.xml[m
[36m@@ -5,7 +5,7 @@[m
         xmlns:app="http://schemas.android.com/apk/res-auto"[m
         android:layout_width="match_parent"[m
         android:layout_height="match_parent"[m
[31m-        tools:context=".SerieBrowseActivity">[m
[32m+[m[32m        tools:context=".SerieBrowseActivity" android:background="@color/cardview_dark_background">[m
 [m
 [m
     <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"[m
[1mdiff --git a/app/src/main/res/layout/comic_search_activity3.xml b/app/src/main/res/layout/comic_search_activity3.xml[m
[1mindex 2597906..585faa3 100644[m
[1m--- a/app/src/main/res/layout/comic_search_activity3.xml[m
[1m+++ b/app/src/main/res/layout/comic_search_activity3.xml[m
[36m@@ -7,7 +7,7 @@[m
         android:layout_width="match_parent"[m
         android:layout_height="match_parent"[m
         tools:context=".HeroSearchActivity"[m
[31m-        tools:background="@android:color/darker_gray">[m
[32m+[m[32m        tools:background="@color/cardview_dark_background">[m
 [m
     <LinearLayout[m
             android:layout_width="match_parent" android:layout_height="wrap_content"[m
[1mdiff --git a/app/src/main/res/layout/hero_search_activity.xml b/app/src/main/res/layout/hero_search_activity.xml[m
[1mindex b97858f..5252089 100644[m
[1m--- a/app/src/main/res/layout/hero_search_activity.xml[m
[1m+++ b/app/src/main/res/layout/hero_search_activity.xml[m
[36m@@ -7,7 +7,7 @@[m
         android:layout_width="match_parent"[m
         android:layout_height="match_parent"[m
         tools:context=".HeroSearchActivity"[m
[31m-        tools:background="@android:color/darker_gray">[m
[32m+[m[32m        android:background="@color/cardview_dark_background">[m
     <LinearLayout android:layout_width="match_parent" android:layout_height="wrap_content"[m
                   android:orientation="vertical"[m
                   app:layout_constraintTop_toTopOf="parent"[m
[36m@@ -20,6 +20,7 @@[m
                 app:layout_constraintStart_toStartOf="parent" android:layout_marginStart="8dp"[m
                 app:layout_constraintEnd_toEndOf="parent" android:layout_marginEnd="8dp"[m
                 android:layout_marginTop="8dp"[m
[32m+[m
                 app:layout_constraintTop_toTopOf="parent" android:id="@+id/heroSearchView"[m
                 android:queryHint="@string/heroSearchViewHint"/>[m
         <android.support.v7.widget.RecyclerView[m
[1mdiff --git a/app/src/main/res/layout/list_row.xml b/app/src/main/res/layout/list_row.xml[m
[1mindex 0de23bb..dc04892 100644[m
[1m--- a/app/src/main/res/layout/list_row.xml[m
[1m+++ b/app/src/main/res/layout/list_row.xml[m
[36m@@ -10,11 +10,11 @@[m
     <android.support.v7.widget.CardView[m
             android:layout_width="wrap_content"[m
             android:layout_height="120dp"[m
[31m-            android:layout_marginBottom="8dp"[m
             app:layout_constraintBottom_toBottomOf="parent" android:layout_marginTop="8dp"[m
             app:layout_constraintTop_toTopOf="parent" app:layout_constraintStart_toStartOf="parent"[m
[31m-            android:layout_marginStart="8dp" app:layout_constraintVertical_bias="0.0"[m
[31m-            app:layout_constraintEnd_toEndOf="parent" android:layout_marginEnd="8dp">[m
[32m+[m[32m            app:layout_constraintVertical_bias="0.0"[m
[32m+[m[32m            app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m            app:cardBackgroundColor="@color/list_row_background">[m
         <RelativeLayout[m
                 android:padding="12dp"[m
                 android:layout_width="match_parent"[m
[36m@@ -34,13 +34,14 @@[m
                       android:layout_alignParentEnd="true"[m
                       android:layout_toEndOf="@+id/picView" android:layout_marginStart="12dp"[m
                       android:layout_marginLeft="12dp" android:layout_marginTop="5dp"[m
[31m-                      android:layout_alignParentTop="true"/>[m
[32m+[m[32m                      android:layout_alignParentTop="true" android:textColor="@color/card_view_text_color"/>[m
             <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:id="@+id/heroId"[m
                       android:text="id"[m
                       android:layout_below="@+id/heroName"[m
                       android:layout_toRightOf="@id/picView" android:layout_toEndOf="@+id/picView"[m
                       android:layout_marginStart="12dp"[m
[31m-                      android:layout_marginTop="5dp" android:layout_marginLeft="12dp"/>[m
[32m+[m[32m                      android:layout_marginTop="5dp" android:layout_marginLeft="12dp"[m
[32m+[m[32m                      android:textColor="@color/card_view_text_color"/>[m
         </RelativeLayout>[m
     </android.support.v7.widget.CardView>[m
 </android.support.constraint.ConstraintLayout>[m
\ No newline at end of file[m
[1mdiff --git a/app/src/main/res/layout/list_row_comic.xml b/app/src/main/res/layout/list_row_comic.xml[m
[1mindex 53c9fa2..bbe35a8 100644[m
[1m--- a/app/src/main/res/layout/list_row_comic.xml[m
[1m+++ b/app/src/main/res/layout/list_row_comic.xml[m
[36m@@ -4,7 +4,7 @@[m
                                              xmlns:tools="http://schemas.android.com/tools"[m
                                              android:layout_width="match_parent"[m
                                              android:layout_height="wrap_content"[m
[31m-                                             tools:background="@android:color/darker_gray">[m
[32m+[m[32m                                             android:background="@color/list_row_background">[m
 [m
     <android.support.v7.widget.CardView[m
             android:layout_width="0dp"[m
[36m@@ -17,7 +17,8 @@[m
         <RelativeLayout[m
                 android:padding="12dp"[m
                 android:layout_width="match_parent"[m
[31m-                android:layout_height="match_parent" android:id="@+id/comic_row">[m
[32m+[m[32m                android:layout_height="match_parent" android:id="@+id/comic_row"[m
[32m+[m[32m                android:background="@color/list_row_background">[m
 [m
             <ImageView[m
                     android:layout_width="wrap_content"[m
[36m@@ -41,14 +42,16 @@[m
                     android:layout_alignParentEnd="true"[m
                     android:layout_toEndOf="@+id/ComicView" android:layout_marginStart="12dp"[m
                     android:layout_marginLeft="12dp" android:layout_marginTop="5dp"[m
[31m-                    android:layout_alignParentTop="true" android:layout_marginEnd="0dp"/>[m
[32m+[m[32m                    android:layout_alignParentTop="true" android:layout_marginEnd="0dp"[m
[32m+[m[32m                    android:textColor="@color/card_view_text_color"/>[m
 [m
             <TextView android:layout_width="match_parent" android:layout_height="wrap_content" android:id="@+id/comicId"[m
                       android:text="id"[m
                       android:layout_below="@+id/comicTitle"[m
                       android:layout_toRightOf="@id/picView" android:layout_toEndOf="@+id/ComicView"[m
                       android:layout_marginStart="12dp"[m
[31m-                      android:layout_marginTop="5dp" android:layout_marginLeft="12dp"/>[m
[32m+[m[32m                      android:layout_marginTop="5dp" android:layout_marginLeft="12dp"[m
[32m+[m[32m                      android:textColor="@color/card_view_text_color"/>[m
 [m
         </RelativeLayout>[m
     </android.support.v7.widget.CardView>[m
[1mdiff --git a/app/src/main/res/values/colors.xml b/app/src/main/res/values/colors.xml[m
[1mindex 4e6fb04..134bc79 100644[m
[1m--- a/app/src/main/res/values/colors.xml[m
[1m+++ b/app/src/main/res/values/colors.xml[m
[36m@@ -8,4 +8,5 @@[m
     <color name="marvel_red">#e92021</color>[m
     <color name="marvel_black">#202020</color>[m
     <color name="card_view_text_color">#FFFFFF</color>[m
[32m+[m[32m    <color name="list_row_background">#2A75B3C9</color>[m
 </resources>[m
