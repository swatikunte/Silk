package com.afollestad.silk.networking;

import android.os.Handler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * A Apache HTTP Client wrapper that makes networking faster and easier.
 *
 * @author Aidan Follestad (afollestad)
 */
public class SilkHttpClient extends SilkHttpBase {

    /**
     * Initializes a new SilkHttpClient.
     */
    public SilkHttpClient() {
        super();
    }

    /**
     * Initializes a new SilkHttpClient.
     *
     * @param handler The handler used to post to a thread for async callbacks.
     */
    public SilkHttpClient(Handler handler) {
        super(handler);
    }

    /**
     * Adds an HTTP header to the client, which will be used for the next request. Headers are cleared after a request is performed.
     */
    public SilkHttpClient addHeader(SilkHttpHeader header) {
        super.mHeaders.add(header);
        return this;
    }

    /**
     * Adds an HTTP header to the client, which will be used for the next request. Headers are cleared after a request is performed.
     */
    public SilkHttpClient addHeader(String name, String value) {
        addHeader(new SilkHttpHeader(name, value));
        return this;
    }


    /**
     * Makes a GET request on the calling thread.
     */
    public SilkHttpResponse get(String url) throws SilkHttpException {
        return performRequest(new HttpGet(url));
    }

    /**
     * Makes a POST request on the calling thread.
     */
    public SilkHttpResponse post(String url) throws SilkHttpException {
        return post(url, null);
    }

    /**
     * Makes a POST request on the calling thread, with a POST entity (body).
     */
    public SilkHttpResponse post(String url, SilkHttpBody body) throws SilkHttpException {
        HttpPost post = new HttpPost(url);
        if (body != null) post.setEntity(body.getEntity());
        return performRequest(post);
    }

    /**
     * Makes a PUT request on the calling thread.
     */
    public SilkHttpResponse put(String url) throws SilkHttpException {
        return put(url, null);
    }

    /**
     * Makes a GET request on the calling thread, with a PUT entity (body).
     */
    public SilkHttpResponse put(String url, SilkHttpBody body) throws SilkHttpException {
        HttpPut post = new HttpPut(url);
        if (body != null) post.setEntity(body.getEntity());
        return performRequest(post);
    }

    /**
     * Makes a DELETE request on the calling thread.
     */
    public SilkHttpResponse delete(String url) throws SilkHttpException {
        return performRequest(new HttpDelete(url));
    }

    // Async methods

    /**
     * Makes a GET request from a separate thread and posts the results to a callback.
     */
    public void getAsync(final String url, final SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        runOnPriorityThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SilkHttpResponse response = get(url);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(response);
                        }
                    });
                } catch (final SilkHttpException e) {
                    e.printStackTrace();
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        });
    }

    /**
     * Makes a POST request from a separate thread and posts the results to a callback.
     */
    public void postAsync(String url, SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        postAsync(url, null, callback);
    }

    /**
     * Makes a POST request from a separate thread (with a POST entity, or body) and posts the results to a callback.
     */
    public void postAsync(final String url, final SilkHttpBody body, final SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        runOnPriorityThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SilkHttpResponse response = post(url, body);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(response);
                        }
                    });
                } catch (final SilkHttpException e) {
                    e.printStackTrace();
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        });
    }

    /**
     * Makes a PUT request from a separate thread and posts the results to a callback.
     */
    public void putAsync(String url, SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        putAsync(url, null, callback);
    }

    /**
     * Makes a PUT request from a separate thread (with a PUT entity, or body) and posts the results to a callback.
     */
    public void putAsync(final String url, final SilkHttpBody body, final SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        runOnPriorityThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SilkHttpResponse response = put(url, body);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(response);
                        }
                    });
                } catch (final SilkHttpException e) {
                    e.printStackTrace();
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }

            }
        });
    }

    /**
     * Makes a DELETE request from a separate thread and posts the results to a callback.
     */
    public void delete(final String url, final SilkHttpCallback callback) {
        if (callback == null) throw new IllegalArgumentException("The callback cannot be null.");
        runOnPriorityThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SilkHttpResponse response = delete(url);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(response);
                        }
                    });
                } catch (final SilkHttpException e) {
                    e.printStackTrace();
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }

            }
        });
    }
}
