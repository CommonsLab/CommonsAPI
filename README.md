#Commons API wrapper

Android API wrapper for [Wikimedia Commons](https://commons.wikimedia.org/wiki/Main_Page).

This library is an efficient way to connect with the Wikimedia Commons [API](https://www.mediawiki.org/wiki/API:Main_page). The focus of this project is to make it easy to access the Commons API for Android developers by providing an accessible way to integrate this API into mobile applications. This wrapper only provides a basic stack of network features in terms of accessing remote APIs.

 I hope it makes it as easy as possible to access the large variety of multimedia files in Wikimedia Commons. 

The API wrapper is built with the help of the [OkHttp](http://square.github.io/okhttp/) networking library. Many thanks to this great contribution. 


### Supported features

*  Login

*  Logout

*  Create Account, captcha support 

*  Load user contributions, Images / Videos / Audios 

*  Upload media files, contributions

*  Support for multiple [CC](https://en.wikipedia.org/wiki/Creative_Commons_license) licenses

*  Search in Wikimedia Commons 

*  Get the latest RSS Feed 
   1. Picture of the day
   2. Media of the day

*  Extra features: 
   1. Load image and video thumbnails 
   2. Monitored upload progress 


### Download 

[![](https://jitpack.io/v/valdio/CommonsAPI.svg)](https://jitpack.io/#valdio/CommonsAPI)



**PERMISSIONS**

Add the following permissions in the `AndroidManifest.xml` file.

```xm
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```




**GRADLE**

**Step 1.** Open `build.gradle(Module: app)` and add the JitPack repository to your build file

```gradle

repositories {
	//...
	maven { url 'https://jitpack.io' }
}
```


**Step 2.** Add the dependency

```gradle
dependencies {
	    compile 'com.github.valdio:CommonsAPI:1.1.4'
}
```


### How to use the Commons API wrapper ?

Create an instance of `Commons` passing a reference to the `Context`.

```java
Commons commons = new Commons(getApplicationContext());
```

To make use of the library call the defined methods from the `commons` instance. The following code snippets are some examples from the API wrapper methods. 

- **Login example**

```java
commons.userLogin("username", "password", new LoginCallback() {
    @Override
    public void onLoginSuccessful() {
      
    }

    @Override
    public void onFailure() {

    }
});
```

- **Picture of the day RSS Feed**

```java
commons.getPictureOfTheDay(new RSS_FeedCallback() {
    @Override
    public void onFeedReceived(ArrayList<FeedItem> items) {
        
    }

    @Override
    public void onError(Exception error) {

    }
});
```

- **Search in Wikimedia Commons**

```java
commons.searchInCommons("Search String", "50", new ContributionsCallback() {
    @Override
    public void onContributionsReceived(ArrayList<Contribution> contributions) {

    }

    @Override
    public void onFailure() {

    }
});
```

This call returns up to 50 `Contribution` objects in a list. The maximal number of contributions is up to 500, or set it to `max` to load the maximal number by default, 500 in this case.  

- **Upload to Commons**


```java
File file = new File(...));// Contribution file

User user = new User();
user.setUsername("username");// all the upload needs the username
commons.uploadContribution(
        file,
        user,
        "title",
        "comment",
        "description",
        ContributionType.IMAGE,
        Licenses.CreativeCommonsAttributionShareAlike30,  // license class 
        R.drawable.upload_icon,
        new UploadCallback() {
            @Override
            public void onMediaUploadedSuccessfully() {
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
```




### License

```

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
