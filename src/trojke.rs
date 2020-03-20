use std::io::{self, BufRead};
pub fn trojke() {
    let mut grid: Vec<Vec<char>> = Vec::new();
    let mut letter_x: Vec<u32> = Vec::new();
    let mut letter_y: Vec<u32> = Vec::new();
    let mut n = 0;
    let mut number_of_letters = 0;

    let stdin = io::stdin();
    for (count, lines) in stdin.lock().lines().map(|l| l.unwrap()).enumerate() {
        let mut chars: Vec<char> = Vec::new();

        if count != 0 {
            for (place, letter) in lines.chars().enumerate() {
                if letter != '.' {
                    number_of_letters += 1;
                    letter_y.push((count - 1) as u32);
                    letter_x.push(place as u32);
                }

                chars.push(letter);
            }
            grid.push(chars);
        } else {
            n = lines.parse().unwrap();
        }
        if count == n {
            let mut count = 0;
            for i in 0..number_of_letters {
                for j in (i + 1)..number_of_letters {
                    let mut dy: i32 = letter_y[j] as i32 - letter_y[i] as i32;
                    let mut dx: i32 = letter_x[j] as i32 - letter_x[i] as i32;

                    let gcd = gcd(dx, dy).abs();
                    dx /= gcd;
                    dy /= gcd;

                    let mut x = letter_x[j] as i32 + dx;
                    let mut y = letter_y[j] as i32 + dy;

                    while 0 <= x && x < n as i32 && 0 <= y && y < n as i32 {
                        if grid[y as usize][x as usize] != '.' {
                            count += 1;
                        }
                        x += dx;
                        y += dy;
                    }
                }
            }
            println!("{}", count);
        }
    }
}

fn gcd(a: i32, b: i32) -> i32 {
    if b == 0 {
        return a;
    } else {
        return gcd(b, a % b);
    }
}
