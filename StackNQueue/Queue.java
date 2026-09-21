/**
 * ============================================================
 *  QUEUE — Cấu trúc dữ liệu Hàng Đợi
 * ============================================================
 *
 *  📌 Khái niệm:
 *  Queue là cấu trúc dữ liệu hoạt động theo nguyên tắc FIFO
 *  (First In, First Out) — phần tử được thêm vào ĐẦU TIÊN
 *  sẽ được lấy ra ĐẦU TIÊN.
 *
 *  🎫 Ví dụ thực tế:
 *  - Xếp hàng mua vé: người đến trước được phục vụ trước
 *  - Hàng đợi máy in: tài liệu gửi trước được in trước
 *  - Tin nhắn trong chat: tin gửi trước hiển thị trước
 *  - BFS (Breadth-First Search): duyệt theo chiều rộng
 *
 *  📊 Độ phức tạp:
 *  ┌──────────────┬──────────┐
 *  │  Thao tác    │ Big-O    │
 *  ├──────────────┼──────────┤
 *  │  enqueue()   │  O(1)    │
 *  │  dequeue()   │  O(1)    │
 *  │  peek()      │  O(1)    │
 *  │  isEmpty()   │  O(1)    │
 *  │  search()    │  O(n)    │
 *  └──────────────┴──────────┘
 *
 *  🔧 Triển khai: Sử dụng Linked List (Danh sách liên kết)
 *  để đảm bảo enqueue và dequeue đều O(1) mà không cần resize.
 *
 *  Cấu trúc:
 *      front                               rear
 *        ↓                                   ↓
 *      [10] → [20] → [30] → [40] → null
 *
 *      Dequeue ← lấy từ front    Enqueue → thêm vào rear
 */
public class Queue {

    // ===== NODE — Đơn vị cơ bản của Linked List =====

    /**
     * Mỗi Node chứa:
     *   - data: giá trị phần tử
     *   - next: con trỏ đến Node kế tiếp
     *
     * Ví dụ Node chứa giá trị 42:
     *   ┌──────┬──────┐
     *   │  42  │ next─┼──→ (Node kế tiếp hoặc null)
     *   └──────┴──────┘
     */
    private static class Node {
        int data;    // Giá trị phần tử
        Node next;   // Con trỏ đến node kế tiếp

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // ===== THUỘC TÍNH =====

    private Node front;  // Con trỏ đến phần tử đầu hàng (sẽ được dequeue trước)
    private Node rear;   // Con trỏ đến phần tử cuối hàng (vừa được enqueue)
    private int count;   // Số phần tử hiện có trong queue

    // ===== CONSTRUCTOR =====

    /**
     * Khởi tạo Queue rỗng.
     *
     * Trạng thái ban đầu:
     *   front = null   (chưa có phần tử nào)
     *   rear  = null
     *   count = 0
     */
    public Queue() {
        this.front = null;
        this.rear = null;
        this.count = 0;
    }

    // ===== CÁC THAO TÁC CHÍNH =====

    /**
     * ENQUEUE — Thêm phần tử vào cuối hàng đợi.
     *
     * Cơ chế hoạt động:
     *   1. Tạo Node mới chứa giá trị value
     *   2. Nếu queue rỗng → front và rear cùng trỏ đến Node mới
     *   3. Nếu queue có phần tử → nối Node mới vào sau rear, rồi cập nhật rear
     *
     * Ví dụ: enqueue(40)
     *   Trước: front→[10]→[20]→[30]→null    rear→[30]
     *   Sau:   front→[10]→[20]→[30]→[40]→null    rear→[40]
     *                                 ↑ nối vào đây
     *
     * @param value giá trị cần thêm vào queue
     */
    public void enqueue(int value) {
        Node newNode = new Node(value);

        if (isEmpty()) {
            // Queue rỗng: cả front và rear đều trỏ đến node mới
            front = newNode;
            rear = newNode;
        } else {
            // Nối node mới vào sau rear, rồi cập nhật rear
            rear.next = newNode;
            rear = newNode;
        }

        count++;
    }

    /**
     * DEQUEUE — Lấy và xóa phần tử ở đầu hàng đợi.
     *
     * Cơ chế hoạt động:
     *   1. Kiểm tra queue có rỗng không
     *   2. Lấy giá trị tại front
     *   3. Di chuyển front sang node kế tiếp
     *   4. Nếu front trở thành null → rear cũng phải null (queue rỗng)
     *
     * Ví dụ: dequeue()
     *   Trước: front→[10]→[20]→[30]→null    rear→[30]
     *   Sau:          front→[20]→[30]→null   rear→[30]   return 10
     *          ↑ [10] bị loại bỏ
     *
     * @return giá trị phần tử vừa được lấy ra
     * @throws RuntimeException nếu queue rỗng
     */
    public int dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue Underflow! Queue đang rỗng, không thể dequeue.");
        }

        int value = front.data;  // Lưu giá trị để trả về
        front = front.next;      // Di chuyển front sang node tiếp theo

        // Nếu front = null, nghĩa là queue đã rỗng → reset rear
        if (front == null) {
            rear = null;
        }

        count--;
        return value;
    }

    /**
     * PEEK — Xem phần tử ở đầu hàng đợi mà KHÔNG xóa.
     *
     * Ví dụ: peek()
     *   Queue: front→[10]→[20]→[30]→null
     *   Return: 10  (queue không thay đổi)
     *
     * @return giá trị phần tử ở đầu queue
     * @throws RuntimeException nếu queue rỗng
     */
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue rỗng! Không có phần tử để peek.");
        }
        return front.data;
    }

    /**
     * isEmpty — Kiểm tra queue có rỗng hay không.
     *
     * Queue rỗng khi front == null (không có node nào)
     *
     * @return true nếu queue rỗng, false nếu còn phần tử
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * size — Trả về số phần tử hiện có trong queue.
     *
     * @return số phần tử trong queue
     */
    public int size() {
        return count;
    }

    // ===== PHƯƠNG THỨC HỖ TRỢ =====

    /**
     * In toàn bộ queue từ front đến rear để dễ hình dung.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("  Queue rỗng!");
            return;
        }

        System.out.print("  FRONT → ");
        Node current = front;
        while (current != null) {
            System.out.print("[" + current.data + "]");
            if (current.next != null) {
                System.out.print(" → ");
            }
            current = current.next;
        }
        System.out.println(" → REAR");
    }

    // ===== DEMO =====

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       QUEUE — Demo các thao tác      ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        Queue queue = new Queue();

        // --- ENQUEUE ---
        System.out.println("▶ Enqueue 10, 20, 30, 40 vào queue:");
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.enqueue(40);
        queue.display();
        System.out.println("  Size = " + queue.size());

        // --- PEEK ---
        System.out.println("\n▶ Peek (xem đầu queue):");
        System.out.println("  Peek = " + queue.peek());
        System.out.println("  (Queue không thay đổi sau peek)");

        // --- DEQUEUE ---
        System.out.println("\n▶ Dequeue (lấy ra phần tử đầu):");
        int dequeued = queue.dequeue();
        System.out.println("  Dequeued = " + dequeued);
        System.out.println("  Queue sau khi dequeue:");
        queue.display();

        // --- DEQUEUE thêm ---
        System.out.println("\n▶ Dequeue tiếp 2 lần:");
        System.out.println("  Dequeued = " + queue.dequeue());
        System.out.println("  Dequeued = " + queue.dequeue());
        System.out.println("  Queue sau khi dequeue 2 lần:");
        queue.display();

        // --- KIỂM TRA RỖNG ---
        System.out.println("\n▶ isEmpty = " + queue.isEmpty());
        System.out.println("  Size = " + queue.size());

        // --- DEQUEUE phần tử cuối ---
        System.out.println("\n▶ Dequeue phần tử cuối cùng:");
        System.out.println("  Dequeued = " + queue.dequeue());
        System.out.println("  isEmpty = " + queue.isEmpty());

        // --- THỬ DEQUEUE KHI RỖNG ---
        System.out.println("\n▶ Thử dequeue khi queue rỗng:");
        try {
            queue.dequeue();
        } catch (RuntimeException e) {
            System.out.println("  ❌ Lỗi: " + e.getMessage());
        }

        System.out.println("\n✅ Demo hoàn tất!");
    }
}
