package com.yesteapea.utils;


/**
 * JsonStrProducer is an object wrapper with a json tostring function <br>
 *
 * If toString function is not defined for <i>obj</i> the following code results in an
 * unhelpful log message like "<i>my message is for obj: ClassName@123232</i>" <br>
 * <pre>LOG.debug("my message is for this obj: {}", obj)</pre>
 *
 *
 * Using JsonStrProducer would produce a helpful json in the log.<br>
 * <pre>LOG.debug("my message is for this obj: {}", JsonStrProducer.make(obj))</pre>
 * NOTE : This does not handle cyclic references. For example, the below code causes a stack overflow<br>
 * <pre>
 static class X {
    int a;
    Map<String, Object> b;
 }
 public static void stackOverflow() {
    X x = new X();
    x.a = 1;
    x.b = new HashMap<String, Object>();
    x.b.put("1", "2");
    x.b.put("1", x);
    System.out.println(JsonStrProducer.make(x));
 }
 * </pre>
 *
 * Created by saiteja on 2/5/16.
 */
public final class JsonStrProducer<T> {
  private final boolean pretty;
  private final T obj;

  private JsonStrProducer(boolean pretty, T obj) {
    this.pretty = pretty;
    this.obj = obj;
  }

  /**
   * Creates a new JsonStrProducer.
   * @param obj Object to wrap around
   */
  public static <T> JsonStrProducer<T> make(T obj) {
    return new JsonStrProducer<T>(false, obj);
  }

  /**
   * Creates a new JsonStrProducer with toString function returning a pretty json.
   * @param obj Object to wrap around
   */
  public static <T> JsonStrProducer<T> makePretty(T obj) {
    return new JsonStrProducer<T>(true, obj);
  }

  /**
   * @return A string representing the wrapped object. If pretty json is required, use {@link #makePretty},
   * else use {@link #make} to create the wrapper.
   */
  @Override
  public String toString() {
    return pretty ?
           YStringUtils.toPrettyJson(obj) :
           YStringUtils.toJson(obj);
  }
}
