package net.okhotnikov.everything.api;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergey Okhotnikov.
 */
public class ChapterRequest {
    public String password;

    @NotNull
    public Set<String> numbers;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(Set<String> numbers) {
        this.numbers = numbers;
    }

    public void prepare (){
        if (numbers == null)
            numbers = new HashSet<>();
        if(numbers.isEmpty())
            numbers.add("1");
    }
}
