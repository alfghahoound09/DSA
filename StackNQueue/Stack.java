/**
 * ============================================================
 *  STACK — Cấu trúc dữ liệu Ngăn Xếp
 * ============================================================
 *
 *  📌 Khái niệm:
 *  Stack là cấu trúc dữ liệu hoạt động theo nguyên tắc LIFO
 *  (Last In, First Out) — phần tử được thêm vào SAU CÙNG
 *  sẽ được lấy ra ĐẦU TIÊN.
 *
 *  🍽️ Ví dụ thực tế:
 *  - Chồng đĩa: đĩa đặt lên trên cùng sẽ được lấy ra trước
 *  - Nút Undo (Ctrl+Z): thao tác gần nhất được hoàn tác trước
 *  - Lịch sử trình duyệt: trang vừa truy cập được quay lại trước
 *  - Gọi hàm đệ quy: hàm được gọi cuối sẽ return trước (call stack)
 *
 *  📊 Độ phức tạp:
 *  ┌──────────────┬──────────┐
 *  │  Thao tác    │ Big-O    │
 *  ├──────────────┼──────────┤
 *  │  push()      │  O(1)*   │  (* amortized khi resize)
 *  │  pop()       │  O(1)    │
 *  │  peek()      │  O(1)    │
 *  │  isEmpty()   │  O(1)    │
 *  │  search()    │  O(n)    │
 *  └──────────────┴──────────┘
 *
 *  🔧 Triển khai: Sử dụng mảng (Array) với khả năng tự mở rộng
 *  khi đầy (dynamic resizing).
 */
public class Stack {

    // ===== THUỘC TÍNH =====

    private int[] data;   // Mảng lưu trữ các phần tử
    private int top;      // Chỉ số (index) của phần tử trên cùng
    // top = -1 nghĩa là stack đang rỗng

    private static final int DEFAULT_CAPACITY = 10; // Kích thước mặc định ban đầu

    // ===== CONSTRUCTOR =====

    /**
     * Khởi tạo Stack rỗng với kích thước mặc định là 10.
     *
     * Trạng thái ban đầu:
     *   data = [_, _, _, _, _, _, _, _, _, _]   (10 ô trống)
     *   top  = -1                               (chưa có phần tử nào)
     */
    public Stack() {
        this.data = new int[DEFAULT_CAPACITY];
        this.top = -1;
    }

    /**
     * Khởi tạo Stack rỗng với kích thước tùy chọn.
     *
     * @param capacity kích thước ban đầu của mảng
     */
    public Stack(int capacity) {
        this.data = new int[capacity];
        this.top = -1;
    }

    // ===== CÁC THAO TÁC CHÍNH =====

    /**
     * PUSH — Thêm phần tử vào đỉnh stack.
     *
     * Cơ chế hoạt động:
     *   1. Kiểm tra mảng đã đầy chưa → nếu đầy thì mở rộng gấp đôi
     *   2. Tăng top lên 1
     *   3. Gán giá trị vào vị trí top
     *
     * Ví dụ: push(42)
     *   Trước: [10, 20, 30, _, _]   top = 2
     *                      ↑ top
     *   Sau:   [10, 20, 30, 42, _]  top = 3
     *                          ↑ top
     *
     * @param value giá trị cần thêm vào stack
     */
    public void push(int value) {
        // Nếu mảng đầy → tạo mảng mới gấp đôi kích thước
        if (top == data.length - 1) {
            resize(data.length * 2);
        }

        // Tăng top rồi gán giá trị
        top++;
        data[top] = value;
    }

    /**
     * POP — Lấy và xóa phần tử ở đỉnh stack.
     *
     * Cơ chế hoạt động:
     *   1. Kiểm tra stack có rỗng không
     *   2. Lấy giá trị tại vị trí top
     *   3. Giảm top xuống 1 (phần tử coi như bị xóa)
     *   4. Thu nhỏ mảng nếu chỉ dùng 1/4 dung lượng (tối ưu bộ nhớ)
     *
     * Ví dụ: pop()
     *   Trước: [10, 20, 30, 42, _]   top = 3
     *                          ↑ top
     *   Sau:   [10, 20, 30, _, _]     top = 2,  return 42
     *                      ↑ top
     *
     * @return giá trị phần tử vừa được lấy ra
     * @throws RuntimeException nếu stack rỗng
     */
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow! Stack đang rỗng, không thể pop.");
        }

        int value = data[top]; // Lưu giá trị để trả về
        top--;                 // "Xóa" phần tử bằng cách giảm top

        // Thu nhỏ mảng nếu chỉ sử dụng 1/4 dung lượng (tránh lãng phí bộ nhớ)
        if (top > 0 && top == data.length / 4) {
            resize(data.length / 2);
        }

        return value;
    }

    /**
     * PEEK — Xem phần tử ở đỉnh stack mà KHÔNG xóa.
     *
     * Ví dụ: peek()
     *   Stack: [10, 20, 30]   top = 2
     *                    ↑ top
     *   Return: 30  (stack không thay đổi)
     *
     * @return giá trị phần tử trên cùng
     * @throws RuntimeException nếu stack rỗng
     */
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack rỗng! Không có phần tử để peek.");
        }
        return data[top];
    }

    /**
     * isEmpty — Kiểm tra stack có rỗng hay không.
     *
     * Stack rỗng khi top == -1 (chưa có phần tử nào được push)
     *
     * @return true nếu stack rỗng, false nếu còn phần tử
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * size — Trả về số phần tử hiện có trong stack.
     *
     * Vì top là index (bắt đầu từ 0), nên số phần tử = top + 1.
     * Ví dụ: top = 2 → có 3 phần tử (index 0, 1, 2)
     *
     * @return số phần tử trong stack
     */
    public int size() {
        return top + 1;
    }

    // ===== PHƯƠNG THỨC HỖ TRỢ =====

    /**
     * resize — Thay đổi kích thước mảng nội bộ.
     *
     * Tạo mảng mới với kích thước newCapacity, copy toàn bộ
     * phần tử từ mảng cũ sang, rồi trỏ data sang mảng mới.
     *
     * Được gọi khi:
     *   - Mảng đầy → mở rộng gấp đôi (push)
     *   - Mảng dùng ≤ 1/4  → thu nhỏ một nửa (pop)
     *
     * @param newCapacity kích thước mới
     */
    private void resize(int newCapacity) {
        int[] newData = new int[newCapacity];
        for (int i = 0; i <= top; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    /**
     * In toàn bộ stack từ đáy lên đỉnh để dễ hình dung.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("  Stack rỗng!");
            return;
        }
        System.out.println("  ┌─────────┐");
        for (int i = top; i >= 0; i--) {
            String marker = (i == top) ? " ← TOP" : "";
            System.out.printf("  │  %4d   │%s%n", data[i], marker);
            if (i > 0) System.out.println("  ├─────────┤");
        }
        System.out.println("  └─────────┘");
    }

    // ===== DEMO =====

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       STACK — Demo các thao tác      ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        Stack stack = new Stack();

        // --- PUSH ---
        System.out.println("▶ Push 10, 20, 30, 40 vào stack:");
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);
        stack.display();
        System.out.println("  Size = " + stack.size());

        // --- PEEK ---
        System.out.println("\n▶ Peek (xem đỉnh stack):");
        System.out.println("  Peek = " + stack.peek());
        System.out.println("  (Stack không thay đổi sau peek)");

        // --- POP ---
        System.out.println("\n▶ Pop (lấy ra phần tử đỉnh):");
        int popped = stack.pop();
        System.out.println("  Popped = " + popped);
        System.out.println("  Stack sau khi pop:");
        stack.display();

        // --- POP thêm ---
        System.out.println("\n▶ Pop tiếp 2 lần:");
        System.out.println("  Popped = " + stack.pop());
        System.out.println("  Popped = " + stack.pop());
        System.out.println("  Stack sau khi pop 2 lần:");
        stack.display();

        // --- KIỂM TRA RỖNG ---
        System.out.println("\n▶ isEmpty = " + stack.isEmpty());
        System.out.println("  Size = " + stack.size());

        // --- POP phần tử cuối ---
        System.out.println("\n▶ Pop phần tử cuối cùng:");
        System.out.println("  Popped = " + stack.pop());
        System.out.println("  isEmpty = " + stack.isEmpty());

        // --- THỬ POP KHI RỖNG ---
        System.out.println("\n▶ Thử pop khi stack rỗng:");
        try {
            stack.pop();
        } catch (RuntimeException e) {
            System.out.println("  ❌ Lỗi: " + e.getMessage());
        }

        System.out.println("\n✅ Demo hoàn tất!");
    }
}
