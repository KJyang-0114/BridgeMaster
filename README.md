# BridgeMaster

A Minecraft Forge mod that enhances bridge building capabilities with optimized controls and smooth operations.

一個優化建橋控制和操作的Minecraft Forge模組。

## Features 功能

- Optimized bridge building controls with intelligent edge detection
  優化的建橋控制與智能邊緣檢測
- Smooth camera adjustments with natural movement simulation
  模擬自然移動的平滑視角調整
- Smart click timing with dynamic CPS adjustment
  根據情況自動調整CPS的智能點擊控制
- Jump-boost mode with enhanced click speed
  跳躍時提升點擊速度的加強模式
- Anti-detection measures with humanized behavior patterns
  具有人性化行為模式的防檢測措施

## Technical Details 技術細節

- Base pitch angle: 79.5° (normal) / 78.0° (jumping)
  基礎視角: 79.5°(普通) / 78.0°(跳躍)
- Click speed ranges 點擊速度範圍:
  - Normal: 7-12 CPS 普通: 7-12 CPS
  - Jumping: 11-16 CPS 跳躍: 11-16 CPS
- Smooth rotation with ±0.3° yaw variation
  平滑旋轉，偏航角變化範圍 ±0.3°
- Edge detection with 30ms response delay
  邊緣檢測，30ms響應延遲

## Requirements 需求

- Minecraft 1.8.9
- Forge 1.8.9-11.15.1.2318
- Java 8

## Installation 安裝方法

1. Install Minecraft 1.8.9 and Forge 
   安裝Minecraft 1.8.9和Forge

2. Place the mod file in the mods folder 
   將模組文件放入mods資料夾
   - Windows: `%appdata%/.minecraft/mods`
   - macOS: `~/Library/Application Support/minecraft/mods`
   - Linux: `~/.minecraft/mods`

3. Launch Minecraft with Forge profile 
   使用Forge配置文件啟動Minecraft

## Development 開發

### Environment Setup 環境設置

1. Install JDK 8
   安裝JDK 8

2. Clone the repository
   克隆儲存庫
   ```bash
   git clone https://github.com/KJyang-0114/BridgeMaster.git
   ```

3. Setup workspace
   設置工作空間
   ```bash
   ./gradlew setupDecompWorkspace
   ```

4. Generate IDE files (optional)
   生成IDE文件（可選）
   ```bash
   # For Eclipse
   ./gradlew eclipse
   
   # For IntelliJ IDEA
   ./gradlew idea
   ```

### Building 構建

1. Build the mod
   構建模組
   ```bash
   ./gradlew build
   ```

2. Find the compiled jar in `build/libs`
   在 `build/libs` 中找到編譯好的jar文件

## Development History 開發歷程

### v1.0.0 (Current Release)
- Initial release with complete functionality
- 完整功能的首次發布
- Features 功能:
  - Optimized bridge building with edge detection
    優化的建橋功能與邊緣檢測
  - Smart click timing (7-12 CPS normal, 11-16 CPS jumping)
    智能點擊時機控制
  - Natural movement simulation
    自然移動模擬
  - Anti-detection measures
    防檢測措施

### Development Stages 開發階段
1. Basic Functionality 基礎功能
   - Initial speed bridge implementation
   - 初始速度建橋實現

2. Direction Detection 方向檢測
   - Added perfect direction detection
   - 添加完美方向檢測
   - Implemented smooth camera movement
   - 實現平滑視角移動

3. Click Optimization 點擊優化
   - Enhanced click timing system
   - 增強點擊時機系統
   - Added jump boost mode
   - 添加跳躍加速模式

4. Final Refinements 最終優化
   - Improved anti-detection measures
   - 改進防檢測措施
   - Natural behavior patterns
   - 自然行為模式
   - Code cleanup and optimization
   - 代碼清理與優化

## Contributing 貢獻

Feel free to submit issues and pull requests.
歡迎提交問題和拉取請求。

## License 許可證

This project is licensed under the MIT License.
本項目採用MIT許可證。

See the [LICENSE](LICENSE) file for details.
詳見[LICENSE](LICENSE)文件。 