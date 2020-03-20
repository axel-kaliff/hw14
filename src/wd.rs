use std::cmp;
use std::io::{self, BufRead};
pub fn wd() {
    let stdin = io::stdin();
    let mut number_of_cases = 0;
    let mut lines_left_on_case = 0;
    let mut s = 0;
    let mut l: Vec<(f64, f64)> = Vec::new();
    for (count, line) in stdin.lock().lines().map(|l| l.unwrap()).enumerate() {
        let nums: Vec<f64> = line
            .split_whitespace()
            .map(|num| num.parse().unwrap())
            .collect();

        if count != 0 {
            if lines_left_on_case == 0 {
                &l.clear();
                s = nums[0] as u32;
                lines_left_on_case = nums[1] as u32;
            } else {
                l.push((nums[0], nums[1]));

                lines_left_on_case -= 1;
                if lines_left_on_case == 0 {
                    let mut sx: f64 = -1.0;
                    let mut sy: f64 = -1.0;

                    for x in 1..s - 1 {
                        for y in 1..s - 1 {
                            let mut max = cmp::min(cmp::min(x, s - x), cmp::min(y, s - y));
                            let mut yes = true;
                            for (i, j) in &l {
                                if (*i == x as f64 && *i == y as f64)
                                    || (max as f64
                                        - ((i - x as f64) * (i - x as f64)
                                            + (j - y as f64) * (j - y as f64))
                                            .sqrt())
                                        < 0.0
                                {
                                    yes = false;
                                }
                            }
                            if yes {
                                sx = x as f64;
                                sy = y as f64;
                            }
                        }
                    }

                    if sx == -1.0 && sy == -1.0 {
                        println!("poodle");
                    } else {
                        println!("{} {}", sx, sy);
                    }
                }
            }
        } else {
            number_of_cases = nums[0] as u32;
        }
    }
}
