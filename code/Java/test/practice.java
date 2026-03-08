import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class practice {
    public static void main(String[] args) {
        calc a = new calc();
        a.CreateCalc();

    }
}

class calc extends JFrame {

    // ======= 入口方法 =======
    public void CreateCalc() {
        initWindow();
        initComponents();  // 只创建/摆放组件
        initEvents();      // 只绑定事件
        setVisible(true);
    }

    // ========== 1) 窗口基础 ==========
    private void initWindow() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(360, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    // ========== 2) 组件创建 + 布局 ==========

    // ======= 组件区 =======
    private JTextField display;

    private JButton[] digitButtons;          // 0-9
    private JButton btnDot, btnEq, btnClear, btnBack, btnSign;
    private JButton btnAdd, btnSub, btnMul, btnDiv;

    // ======= 状态区 =======
    private Double firstNumber = null;       // 第一个数
    private String operator = null;          // 当前运算符
    private boolean startNewNumber = true;   // 是否开始输入新数字（运算符后/等号后）
    
    // ======= 布局 =======
    private void initComponents() {
        // 显示屏（右对齐，禁编辑）
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        // macOS 推荐 Menlo；Windows 可用 Consolas；这里用 Monospaced 最稳
        display.setFont(new Font("Monospaced", Font.BOLD, 30));
        display.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(display, BorderLayout.NORTH);

        // 数字按钮
        digitButtons = new JButton[10];
        for (int i = 0; i <= 9; i++) digitButtons[i] = createButton(String.valueOf(i));

        // 功能按钮
        btnDot = createButton(".");
        btnEq = createButton("=");
        btnClear = createButton("C");
        btnBack = createButton("⌫");
        btnSign = createButton("±");

        // 运算按钮
        btnAdd = createButton("+");
        btnSub = createButton("-");
        btnMul = createButton("*");
        btnDiv = createButton("/");

        // 键盘布局：5行4列
        JPanel keypad = new JPanel(new GridLayout(5, 4, 8, 8));
        keypad.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Row1: C  ⌫  ±  /
        keypad.add(btnClear);
        keypad.add(btnBack);
        keypad.add(btnSign);
        keypad.add(btnDiv);

        // Row2: 7 8 9 *
        keypad.add(digitButtons[7]);
        keypad.add(digitButtons[8]);
        keypad.add(digitButtons[9]);
        keypad.add(btnMul);

        // Row3: 4 5 6 -
        keypad.add(digitButtons[4]);
        keypad.add(digitButtons[5]);
        keypad.add(digitButtons[6]);
        keypad.add(btnSub);

        // Row4: 1 2 3 +
        keypad.add(digitButtons[1]);
        keypad.add(digitButtons[2]);
        keypad.add(digitButtons[3]);
        keypad.add(btnAdd);

        // Row5: 0  .  =  (空位)
        keypad.add(digitButtons[0]);
        keypad.add(btnDot);
        keypad.add(btnEq);
        keypad.add(new JLabel("")); // 留空：你可替换成 % 等

        add(keypad, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Monospaced", Font.BOLD, 22));
        b.setFocusPainted(false);
        return b;
    }

    // ========== 3) 事件绑定 ==========
    private void initEvents() {
        // 数字
        for (int i = 0; i <= 9; i++) {
            final int d = i;
            digitButtons[i].addActionListener(e -> onDigit(d));
        }

        // 小数点/功能
        btnDot.addActionListener(e -> onDot());
        btnClear.addActionListener(e -> onClear());
        btnBack.addActionListener(e -> onBackspace());
        btnSign.addActionListener(e -> onToggleSign());

        // 运算
        btnAdd.addActionListener(e -> onOperator("+"));
        btnSub.addActionListener(e -> onOperator("-"));
        btnMul.addActionListener(e -> onOperator("*"));
        btnDiv.addActionListener(e -> onOperator("/"));

        // 等号
        btnEq.addActionListener(e -> onEquals());
    }

    // ========== 输入与显示（含“滑动到末尾”） ==========
    private void setDisplayText(String s) {
        display.setText(s);
        // 让文本自动“向左滚动”，始终显示末尾（像计算器那样）
        display.setCaretPosition(s.length());
    }

    private String getDisplayText() {
        return display.getText();
    }

    // ========== 事件逻辑 ==========
    private void onDigit(int d) {
        String cur = getDisplayText();

        if (startNewNumber) {
            setDisplayText(String.valueOf(d));
            startNewNumber = false;
            return;
        }

        // 避免前导 0： "0" 后输入数字 -> 替换
        if ("0".equals(cur) && !cur.contains(".")) {
            setDisplayText(String.valueOf(d));
        } else {
            setDisplayText(cur + d);
        }
    }

    private void onDot() {
        String cur = getDisplayText();

        // 新数字开头按点：变成 0.
        if (startNewNumber) {
            setDisplayText("0.");
            startNewNumber = false;
            return;
        }

        // 已经有点就不再添加
        if (cur.contains(".")) return;

        setDisplayText(cur + ".");
    }

    private void onBackspace() {
        if (startNewNumber) return;

        String cur = getDisplayText();
        if (cur.length() <= 1) {
            setDisplayText("0");
            startNewNumber = true;
            return;
        }

        // 删除最后一个字符
        String next = cur.substring(0, cur.length() - 1);

        // 处理 "-" 这种残留
        if ("-".equals(next) || next.isEmpty()) {
            setDisplayText("0");
            startNewNumber = true;
        } else {
            setDisplayText(next);
        }
    }

    private void onToggleSign() {
        String cur = getDisplayText();

        // 如果准备输入新数字，± 直接把它变成 -0 并进入输入状态
        if (startNewNumber) {
            setDisplayText("-0");
            startNewNumber = false;
            return;
        }

        if ("0".equals(cur) || "0.".equals(cur)) return;

        if (cur.startsWith("-")) setDisplayText(cur.substring(1));
        else setDisplayText("-" + cur);
    }

    private void onOperator(String op) {
        // 连续按运算符：替换运算符
        if (operator != null && startNewNumber) {
            operator = op;
            return;
        }

        Double current = parseDisplayAsNumber();
        if (current == null) return;

        // 支持连算：已有 first 和 operator，则先算一次
        if (firstNumber != null && operator != null) {
            Double result = compute(firstNumber, current, operator);
            if (result == null) return;
            displayNumber(result);
            firstNumber = result;
        } else {
            firstNumber = current;
        }

        operator = op;
        startNewNumber = true;
    }

    private void onEquals() {
        if (operator == null || firstNumber == null) {
            showError("请先输入：数字 → 运算符 → 数字，再按 =");
            return;
        }

        Double second = parseDisplayAsNumber();
        if (second == null) return;

        Double result = compute(firstNumber, second, operator);
        if (result == null) return;

        displayNumber(result);

        // 等号后：结果保留为 first，清掉 operator
        firstNumber = result;
        operator = null;
        startNewNumber = true;
    }

    private void onClear() {
        setDisplayText("0");
        firstNumber = null;
        operator = null;
        startNewNumber = true;
    }

    // ========== 工具：解析、计算、显示、报错 ==========
    private Double parseDisplayAsNumber() {
        try {
            return Double.parseDouble(getDisplayText());
        } catch (NumberFormatException ex) {
            showError("数字格式错误：\"" + getDisplayText() + "\"");
            onClear();
            return null;
        }
    }

    private Double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0.0) {
                    showError("除数不能为 0");
                    return null;
                }
                return a / b;
            default:
                showError("未知运算符：" + op);
                return null;
        }
    }

    private void displayNumber(double x) {
        // 简单去掉 .0：12.0 -> 12
        if (x == (long) x) setDisplayText(String.valueOf((long) x));
        else setDisplayText(String.valueOf(x));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "错误", JOptionPane.ERROR_MESSAGE);
    }
}