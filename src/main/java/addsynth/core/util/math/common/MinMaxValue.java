package addsynth.core.util.math.common;

// UNUSED: MinMaxValue
public class MinMaxValue {

  public double min;
  public double max;
  public double half;

  public MinMaxValue(double min, double max){
    this.min = Math.min(min, max);
    this.max = Math.max(min, max);
    updateHalf();
  }

  public final void setMin(double min){
    this.min = min;
    max = Math.max(min, max);
    updateHalf();
  }

  public final void setMax(double max){
    this.max = min;
    min = Math.min(min, max);
    updateHalf();
  }

  private final void updateHalf(){
    half = (max - min)/2;
  }

}
